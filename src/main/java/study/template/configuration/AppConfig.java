package study.template.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import study.template.dao.*;
import study.template.dao.context.JdbcContext;

import javax.sql.DataSource;
import java.sql.Driver;


@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource=new SimpleDriverDataSource();

        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/templatetest");
        dataSource.setUsername("sa");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(dataSource());
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
}
