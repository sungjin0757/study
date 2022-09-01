package basic.springbatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class SkipCheckListener extends StepExecutionListenerSupport {
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = genExitCode(stepExecution.getExitStatus());
        if(!isFailed(exitCode) && isSkip(stepExecution))
            return new ExitStatus("NOT FAILED WITH SKIPS");

        return null;
    }

    private String genExitCode(ExitStatus exitStatus) {
        return exitStatus.getExitCode();
    }

    private boolean isFailed(String exitCode) {
        return exitCode.equals(ExitStatus.FAILED.getExitCode());
    }

    private boolean isSkip(StepExecution stepExecution) {
        if(stepExecution.getSkipCount() > 0)
            return true;
        return false;
    }
}
