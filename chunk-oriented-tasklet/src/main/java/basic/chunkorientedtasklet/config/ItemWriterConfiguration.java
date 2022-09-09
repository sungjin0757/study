package basic.chunkorientedtasklet.config;

import basic.chunkorientedtasklet.domain.JpaWriterTestUser;
import basic.chunkorientedtasklet.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemWriterConfiguration {
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemWriter<User> customLogUserItemWriter() {
        return items -> {
            for (User user : items) {
                log.info("User = {}", user);
            }
        };
    }

    @Bean
    public JdbcBatchItemWriter<User> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("insert into jdbc_users(username, age) values (:userName, :age)")
                .beanMapped()
                .build();
    }

    @Bean
    public JpaItemWriter<JpaWriterTestUser> jpaItemWriter() {
        return new JpaItemWriterBuilder<JpaWriterTestUser>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
