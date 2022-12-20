package basic.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class DeciderConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * firstStep -> decider(ODD, EVEN) -> 상태에 따라 ODD, EVEN STEP -> End
     */
    @Bean
    public Job deciderJob() {
        return jobBuilderFactory.get("decider job")
                .start(deciderFirstStep())
                .next(decider()) // 홀, 짝 구분
                .from(decider()) // decider의 상태
                    .on("ODD") // ODD 라면
                    .to(oddStep())
                .from(decider())
                    .on("EVEN")
                    .to(evenStep())
                .end().build();
    }

    @Bean
    public Step deciderFirstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is First Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is Even Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is Odd Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new EvenOddDecider();
    }

    public static class EvenOddDecider implements JobExecutionDecider {
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            Random random = new Random();

            int randomNumber = random.nextInt();
            log.info("Random Number = {}",randomNumber);

            return createEvenOddFlowExecutionStatus(randomNumber);
        }

        private FlowExecutionStatus createEvenOddFlowExecutionStatus(int num) {
            if(isOdd(num))
                return new FlowExecutionStatus("ODD");
            return new FlowExecutionStatus("EVEN");
        }

        private boolean isOdd(int num) {
            if(num % 2 == 1)
                return true;
            return false;
        }
    }

}
