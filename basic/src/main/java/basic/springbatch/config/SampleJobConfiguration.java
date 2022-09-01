package basic.springbatch.config;

import basic.springbatch.listener.SampleJobListener;
import basic.springbatch.listener.SampleStepListener;
import basic.springbatch.service.SecondTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SecondTasklet secondTasklet;
    private final SampleJobListener sampleJobListener;
    private final SampleStepListener sampleStepListener;

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get("First Job") // First Job이라는 이름의 batch job생성
                .incrementer(new RunIdIncrementer())
                .listener(sampleJobListener)
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    @Bean
    @JobScope
    public Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet((stepContribution, chunkContext) -> {
                    Map<String, Object> jobExecutionContext = chunkContext.getStepContext()
                            .getJobExecutionContext();
                    log.info(jobExecutionContext.toString());
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
                .listener(sampleStepListener)
                .build();
    }

}
