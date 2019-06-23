package com.hosmos.linkind.configurations;

import com.hosmos.linkind.batches.DetailProcessorTasklet;
import com.hosmos.linkind.batches.DetailWriterTasklet;
import com.hosmos.linkind.batches.DetailReaderTasklet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfigurations {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicBoolean enabled = new AtomicBoolean(true);

    private AtomicInteger batchRunCounter = new AtomicInteger(0);

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    private JobBuilderFactory jobs;

    @Autowired
    public void setJobs(JobBuilderFactory jobs) {
        this.jobs = jobs;
    }

    private StepBuilderFactory steps;

    @Autowired
    public void setSteps(StepBuilderFactory steps) {
        this.steps = steps;
    }

    // # Main logic

    @Bean
    public Tasklet readerTasklet() {
        return new DetailReaderTasklet();
    }

    @Bean
    public Tasklet writerTasklet() {
        return new DetailWriterTasklet();
    }

    @Bean
    public Tasklet processorTasklet() {
        return new DetailProcessorTasklet();
    }

    @Bean
    protected Step readDetails() {
        return steps.get("readDetails").tasklet(readerTasklet()).build();
    }

    @Bean
    protected Step processDetails() {
        return steps.get("readDetails").tasklet(processorTasklet()).build();
    }

    @Bean
    protected Step writeDetails() {
        return steps.get("readDetails").tasklet(writerTasklet()).build();
    }

    @Bean
    public Job job() {
        return jobs.get("ipDetailsJob")
                .start(readDetails())
                .next(processDetails())
                .next(writeDetails())
                .build();
    }

    // Each 2 hours
    @Scheduled(fixedDelay = 7200000, initialDelay = 10000)
    public void launchJob() throws Exception {
        Date date = new Date();
        logger.debug("scheduler starts at " + date);
        if (enabled.get()) {
            JobExecution jobExecution = jobLauncher().run(job(), new JobParametersBuilder().addDate("launchDate", date)
                    .toJobParameters());
            batchRunCounter.incrementAndGet();
            logger.debug("Batch job ends with status as " + jobExecution.getStatus());
        }
        logger.debug("scheduler ends ");
    }

    public void stop() {
        enabled.set(false);
    }

    public void start() {
        enabled.set(true);
    }

    @Bean
    public TaskScheduler poolScheduler() {
        return new CustomTaskScheduler();
    }

    private class CustomTaskScheduler extends ThreadPoolTaskScheduler {

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }

    }

    public void cancelFutureSchedulerTasks() {
        scheduledTasks.forEach((k, v) -> {
            if (k instanceof BatchConfigurations) {
                v.cancel(false);
            }
        });
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        factory.setTransactionManager(new ResourcelessTransactionManager());
        return (JobRepository) factory.getObject();
    }

    public AtomicInteger getBatchRunCounter() {
        return batchRunCounter;
    }
}
