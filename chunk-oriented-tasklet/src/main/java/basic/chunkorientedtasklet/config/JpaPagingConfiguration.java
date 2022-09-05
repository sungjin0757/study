package basic.chunkorientedtasklet.config;

import basic.chunkorientedtasklet.domain.User;
import basic.chunkorientedtasklet.listener.LogJobListener;
import basic.chunkorientedtasklet.service.CustomLogUserItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomLogUserItemWriter customLogUserItemWriter;
    private final LogJobListener logJobListener;
    private final EntityManagerFactory entityManagerFactory;

    private final static int CHUNK_SIZE = 10;

    @Bean
    public Job jpaPagingJob() {
        return jobBuilderFactory.get("Paging Item Job - JPA")
                .incrementer(new RunIdIncrementer())
                .listener(logJobListener)
                .start(jpaPagingStep())
                .build();
    }

    @Bean
    public Step jpaPagingStep() {
        return stepBuilderFactory.get("Paging Item Step - JPA")
                .<User, User>chunk(10)
                .reader(jpaPagingItemReader())
                .writer(customLogUserItemWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<User> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("Paging Item Reader - JPA")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("select u from User u")
                .build();
    }
}
