package com.hosmos.linkind.batches;

import com.hosmos.linkind.models.IpDetail;
import com.hosmos.linkind.utils.ExtractorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DetailProcessorTasklet implements Tasklet, StepExecutionListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<String> ips;
    private List<IpDetail> details;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ips = (List<String>) stepExecution.getJobExecution().getExecutionContext().get("ips");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.trace("start processing ips...");
        this.details = new ArrayList<>();

        int lastProcessedIndex = 0;
        while (lastProcessedIndex < this.ips.size()) {

            int fromIndex = lastProcessedIndex;
            int toIndex = calculateToIndex(lastProcessedIndex);

            System.out.println("from index: " + fromIndex);
            System.out.println("to index: " + toIndex);

            this.details.addAll(ExtractorUtils.fetchIpsDetail(this.ips.subList(lastProcessedIndex, toIndex)));

//            Because of Ip-Api website limits
            Thread.sleep(1500);

            lastProcessedIndex = toIndex;
        }

        return RepeatStatus.FINISHED;
    }

    private int calculateToIndex(int lastProcessedIndex) {
        int toIndex = lastProcessedIndex + 100;
        return (toIndex < this.ips.size()) ? toIndex : this.ips.size();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put("details", this.details);
        logger.trace("fetching details ended.");
        return ExitStatus.COMPLETED;
    }
}
