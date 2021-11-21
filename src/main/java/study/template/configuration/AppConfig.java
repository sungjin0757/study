package study.template.configuration;

import lombok.RequiredArgsConstructor;
import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import study.template.dao.*;
import study.template.dao.context.JdbcContext;
import study.template.dao.context.JdbcContextV2;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final Environment env;

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource=new SimpleDriverDataSource();

        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }


    @Bean
    public JdbcOperations jdbcOperations(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(dataSource());
    }

    @Bean
    public JdbcContextV2 jdbcContextV2(){
        return new JdbcContextV2(dataSource());
    }

    @Bean
    public UserDaoV1 userDaoV1(){
        return new UserDaoV1(dataSource());
    }

    @Bean
    public UserDaoV2 userDaoV2(){
        return new UserDaoV2(dataSource());
    }

    @Bean
    public UserDaoV4 userDaoV4(){
        return new UserDaoV4(dataSource());
    }

    @Bean
    public UserDaoV5 userDaoV5(){
        return new UserDaoV5(dataSource());
    }

    @Bean
    public UserDaoV6 userDaoV6(){
        return new UserDaoV6(jdbcContext(),dataSource());
    }

    @Bean
    public UserDaoV7 userDaoV7(){
        return new UserDaoV7(jdbcContext(),dataSource());
    }

    @Bean
    public UserDaoV8 userDaoV8(){
        return new UserDaoV8(jdbcContextV2(),dataSource());
    }

    @Bean
    public UserDaoV9 userDaoV9(){
        return new UserDaoV9(jdbcOperations());
    }
}
