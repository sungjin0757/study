package basic.springbatch.config;

import basic.springbatch.service.SecondTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SecondTasklet secondTasklet;

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get("First Job") // First Job이라는 이름의 batch job생성
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    @Bean
    @JobScope
    public Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("This Is First Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step secondStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(secondTasklet)
                .build();
    }

}
