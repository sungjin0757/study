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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final CustomLogUserItemWriter customLogUserItemWriter;
    private final LogJobListener logJobListener;

    private static final int CHUNK_SIZE = 10;

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
                .<User, User>chunk(CHUNK_SIZE)
                .reader(jdbcCursorItemReader())
                .writer(customLogUserItemWriter)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<User> jdbcCursorItemReader() {
        JdbcCursorItemReader<User> jdbcCursorItemReader = new JdbcCursorItemReader<>();
        return new JdbcCursorItemReaderBuilder<User>()
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper((rs, rowNum) -> {
                    Long user_id = rs.getLong("user_id");
                    String userName = rs.getString("username");
                    int age = rs.getInt("age");
                    return new User(user_id, userName, age);
                })
                .sql("SELECT user_id, username, age FROM users")
                .name("Cursor Item Reader - JDBC")
                .build();
    }
}
