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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DetailWriterTasklet implements Tasklet, StepExecutionListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<IpDetail> details;
    private LinkMapper mapper;

    @Autowired
    public void setMapper(LinkMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.details = (List<IpDetail>) stepExecution.getJobExecution().getExecutionContext().get("details");
    }

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.trace("start updating ips...");

        for (IpDetail detail: this.details) {
            if (detail.getStatus().equals("success")) {
                mapper.updateIpDetail(detail);
            } else {
                mapper.deleteWrongIpFromVisits(detail.getQuery());
            }
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.trace(details.size() + " updated. batch successfully ended.");
        return ExitStatus.COMPLETED;
    }
}
