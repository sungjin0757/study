package study.template.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.template.dao.ConnectionMaker;
import study.template.dao.ConnectionMakerImpl;
import study.template.dao.UserDao;

@Configuration
public class AppConfig {

    @Bean
    public ConnectionMaker connectionMaker(){
        return new ConnectionMakerImpl();
    }

    @Bean
    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }
}
