package study.serviceabstraction.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import study.serviceabstraction.dao.UserDao;
import study.serviceabstraction.dao.UserDaoImpl;
import study.serviceabstraction.service.UserService;

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
    public UserDao userDao(){
        return new UserDaoImpl(jdbcOperations());
    }

    @Bean
    public UserService userService(){
        return new UserService(userDao());
    }
}
