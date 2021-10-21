package study.template.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import study.template.dao.ConnectionMaker;
import study.template.dao.ConnectionMakerImpl;
import study.template.dao.UserDaoV1;
import study.template.dao.UserDaoV2;

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
    public UserDaoV2 userDao(){
        return new UserDaoV2(dataSource());
    }
}
