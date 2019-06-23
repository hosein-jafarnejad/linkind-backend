package com.hosmos.linkind.batches;

import com.hosmos.linkind.dao.LinkMapper;
import com.hosmos.linkind.models.IpDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DetailReaderTasklet implements Tasklet, StepExecutionListener {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<String> ips;

    private LinkMapper mapper;

    @Autowired
    public void setMapper(LinkMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.trace("fetch ip of visitors with no ip detail.");
        ips = mapper.getIpOfVisitorsWithNoIpDetail();
        logger.trace(ips.size() + " ips with nop details fetched.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        return RepeatStatus.FINISHED;
    }



    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put("ips", this.ips);
        logger.trace("ip reader ended.");
        return ExitStatus.COMPLETED;
    }
}
