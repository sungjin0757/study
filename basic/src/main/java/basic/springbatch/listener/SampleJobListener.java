package basic.springbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Before Job " + jobExecution.getJobInstance().getJobName());
        log.info("Job Params " + jobExecution.getJobParameters());
        log.info("Job Exec Context " + jobExecution.getExecutionContext());

        jobExecution.getExecutionContext().put("Dummy", "Dummy Value");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("After Job " + jobExecution.getJobInstance().getJobName());
        if (isFail(jobExecution)) {
            log.info(jobExecution.getJobInstance().getJobName() + "Failed!");
            return;
        }
        log.info("Job Params " + jobExecution.getJobParameters());
        log.info("Job Exec Context " + jobExecution.getExecutionContext());
    }

    private boolean isFail(JobExecution jobExecution) {
        return jobExecution.getStatus() == BatchStatus.FAILED;
    }
}
