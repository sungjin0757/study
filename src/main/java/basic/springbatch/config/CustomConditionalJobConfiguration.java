package basic.springbatch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CustomConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Scenario 1. step1(Failed) -> end
     * Scenario 2. step1(NOT FAILED WITH SKIPS) -> errorStep -> end
     * Scenario 3. step1 -> step2 -> end
     */
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                    .on("FAILED")
                    .end()
                .from(step1())
                    .on("NOT FAILED WITH SKIPS") // Custom Exit Status
                    .to(errorStep())
                    .on("*")
                    .end()
                .from(step1())
                    .on("*")
                    .to(step2())
                    .on("*")
                    .end()
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("First Step")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is First Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("Second Step")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is Second Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step errorStep() {
        return stepBuilderFactory.get("Error Step")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("This Is Error Step");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
