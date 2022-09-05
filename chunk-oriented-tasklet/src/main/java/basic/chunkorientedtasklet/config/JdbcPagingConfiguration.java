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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final CustomLogUserItemWriter customLogUserItemWriter;
    private final LogJobListener logJobListener;

    private static final int CHUNK_SIZE = 10;

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
                .<User, User>chunk(CHUNK_SIZE)
                .reader(jdbcPagingItemReader())
                .writer(customLogUserItemWriter)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<User> jdbcPagingItemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<User>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(((rs, rowNum) -> {
                    Long user_id = rs.getLong("user_id");
                    String userName = rs.getString("username");
                    int age = rs.getInt("age");
                    return new User(user_id, userName, age);
                }))
                .queryProvider(genQueryProvider())
                .name("Paging Item Reader - JDBC")
                .build();
    }

    @Bean
    public PagingQueryProvider genQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);
        queryProviderFactoryBean.setSelectClause("user_id, username, age");
        queryProviderFactoryBean.setFromClause("from users");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("user_id", Order.DESCENDING);

        queryProviderFactoryBean.setSortKeys(sortKeys);

        return queryProviderFactoryBean.getObject();
    }
}
