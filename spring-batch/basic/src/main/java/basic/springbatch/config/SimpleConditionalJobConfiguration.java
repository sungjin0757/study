package basic.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Scenario 1. FailedCondition(실패시) -> Step1
     * Scenario 2. FailedCondition(성공시) -> Step1 -> Step2
     */
    @Bean
    public Job conditionalJob() {
        return jobBuilderFactory.get("conditionalJob")
                .start(failedConditionalStep())
                    .on("FAILED") // failed 일 경우
                    .to(conditionalStep1()) // step1 실행
                    .on("*") // step1의 결과와 관계 없이
                    .end() // flow 종료
                .from(failedConditionalStep()) // failedConditionalStep 으로 부터
                    .on("*") // failed 외에 모든 경우
                    .to(conditionalStep1())
                    .next(conditionalStep2())
                    .on("*") // step2 의 결과와 상관 없이
                    .end()
                .end()
                .build();
    }

    /**
     * 의도적인 FAILED Step.
     * Job flow 에서 활용
     */
    @Bean
    public Step failedConditionalStep() {
        return stepBuilderFactory.get("failed step")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is Failed Step");
                    /**
                     * ExitStatus를 Failed로
                     */
                    stepContribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is First Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is Second Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
