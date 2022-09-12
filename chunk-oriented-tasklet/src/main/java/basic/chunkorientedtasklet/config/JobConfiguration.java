package basic.chunkorientedtasklet.config;

import basic.chunkorientedtasklet.common.property.BatchJobProperty;
import basic.chunkorientedtasklet.domain.JpaWriterTestUser;
import basic.chunkorientedtasklet.domain.User;
import basic.chunkorientedtasklet.listener.LogJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LogJobListener logJobListener;
    private final JdbcCursorItemReader<User> jdbcCursorItemReader;
    private final JdbcPagingItemReader<User> jdbcPagingItemReader;
    private final JpaPagingItemReader<User> jpaPagingItemReader;
    private final ItemWriter<User> customLogUserItemWriter;
    private final JdbcBatchItemWriter<User> jdbcBatchItemWriter;
    private final ItemProcessor<User, JpaWriterTestUser> itemProcessor;
    private final JpaItemWriter<JpaWriterTestUser> jpaItemWriter;

    @Bean
    public Job jdbcCursorItemJob() {
        return jobBuilderFactory.get("Cursor Item Job - JDBC")
                .incrementer(new RunIdIncrementer())
                .listener(logJobListener)
                .start(jdbcCursorItemStep())
                .build();
    }

    @Bean
    public Step jdbcCursorItemStep() {
        return stepBuilderFactory.get("Cursor item Step - JDBC")
                .<User, User>chunk(BatchJobProperty.CHUNK_SIZE)
                .reader(jdbcCursorItemReader)
                .writer(jdbcBatchItemWriter)
                .build();
    }

    @Bean
    public Job jdbcPagingJob() throws Exception{
        return jobBuilderFactory.get("Paging Job - JDBC")
                .incrementer(new RunIdIncrementer())
                .listener(logJobListener)
                .start(jdbcPagingStep())
                .build();
    }

    @Bean
    public Step jdbcPagingStep() throws Exception {
        return stepBuilderFactory.get("Paging Job - JDBC")
                .<User, User>chunk(BatchJobProperty.CHUNK_SIZE)
                .reader(jdbcPagingItemReader)
                .writer(customLogUserItemWriter)
                .build();
    }

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
                .<User, JpaWriterTestUser>chunk(10)
                .reader(jpaPagingItemReader)
                .processor(itemProcessor)
                .writer(jpaItemWriter)
                .build();
    }

}
