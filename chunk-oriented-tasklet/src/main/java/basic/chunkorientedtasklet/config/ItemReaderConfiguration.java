package basic.chunkorientedtasklet.config;

import basic.chunkorientedtasklet.common.property.BatchJobProperty;
import basic.chunkorientedtasklet.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemReaderConfiguration {
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public JdbcCursorItemReader<User> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<User>()
                .fetchSize(BatchJobProperty.CHUNK_SIZE)
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

    @Bean
    public JdbcPagingItemReader<User> jdbcPagingItemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<User>()
                .pageSize(BatchJobProperty.CHUNK_SIZE)
                .fetchSize(BatchJobProperty.CHUNK_SIZE)
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

    @Bean
    public JpaPagingItemReader<User> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<User>()
                .name("Paging Item Reader - JPA")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(BatchJobProperty.CHUNK_SIZE)
                .queryString("select u from User u")
                .build();
    }
}
