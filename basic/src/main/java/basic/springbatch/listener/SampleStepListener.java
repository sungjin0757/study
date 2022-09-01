package basic.springbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Before Step + " + stepExecution.getStepName());
        log.info("Before Step - Job Execution Context + " + stepExecution.getJobExecution().getExecutionContext());
        log.info("Before Step - Step Execution Context + " + stepExecution.getExecutionContext());

        stepExecution.getExecutionContext()
                .put("Step Dummy", "Step Dummy Value");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("After Step + " + stepExecution.getStepName());
        log.info("After Step - Job Execution Context + " + stepExecution.getJobExecution().getExecutionContext());
        log.info("After Step - Step Execution Context + " + stepExecution.getExecutionContext());
        return null;
    }
}

