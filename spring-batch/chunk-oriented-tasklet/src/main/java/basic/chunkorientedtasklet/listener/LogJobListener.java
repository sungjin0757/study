package basic.chunkorientedtasklet.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LogJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("========== {} Job Started ==========", jobExecution.getJobInstance().getJobName());
        log.info("Job Params " + jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("========== {} Job Ended ==========", jobExecution.getJobInstance().getJobName());
        if (isFailed(jobExecution)) {
            log.warn("{} Job Failed!", jobExecution.getJobInstance().getJobName());
        }
        log.info("Job Params " + jobExecution.getJobParameters());
    }

    private boolean isFailed(JobExecution jobExecution) {
        return jobExecution.getStatus() == BatchStatus.FAILED;
    }
}
