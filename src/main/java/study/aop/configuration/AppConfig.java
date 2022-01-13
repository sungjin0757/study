package study.aop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.*;
import study.aop.proxy.advice.TransactionAdvice;
import study.aop.proxy.pointcut.NameMatchClassMethodPointcut;
import study.aop.service.*;
import study.aop.dao.UserDao;
import study.aop.dao.UserDaoImpl;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
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
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
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
    public MailSender mailSender(){
        return new DummyMailSender();
    }

//    @Bean
//    public TransactionAdvice transactionAdvice(){
//        return new TransactionAdvice(transactionManager());
//    }

//    @Bean
//    public TransactionInterceptor transactionAdvice(){
//        TransactionInterceptor transactionInterceptor=new TransactionInterceptor();
//
//        Properties properties=new Properties();
//        properties.put("get*", "PROPAGATION_REQUIRED,readOnly");
//        properties.put("*","PROPAGATION_REQUIRED");
//
//        transactionInterceptor.setTransactionManager(transactionManager());
//        transactionInterceptor.setTransactionAttributes(properties);
//
//        return transactionInterceptor;
//    }

//    @Bean
//    public NameMatchMethodPointcut transactionPointcut(){
//        NameMatchMethodPointcut pointcut=new NameMatchMethodPointcut();
//        pointcut.setMappedNames("upgrade*");
//        return pointcut;
//    }

//    @Bean
//    public NameMatchClassMethodPointcut transactionPointcut(){
//        NameMatchClassMethodPointcut pointcut=new NameMatchClassMethodPointcut();
//        pointcut.setMappedClassName("*ServiceImpl");
//        pointcut.setMappedNames("upgrade*");
//        return pointcut;
//    }

//    @Bean
//    public AspectJExpressionPointcut transactionPointcut(){
//        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
//        pointcut.setExpression("bean(*Service)");
//        return pointcut;
//    }
//
//    @Bean
//    public DefaultPointcutAdvisor transactionAdvisor(){
//        return new DefaultPointcutAdvisor(transactionPointcut(),transactionAdvice());
//    }
//
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        return new DefaultAdvisorAutoProxyCreator();
//    }

//    @Bean
//    public TransactionFactoryBean userService(){
//        return new TransactionFactoryBean(userServiceImpl(),transactionManager()
//        ,"upgradeLevels()",UserService.class);
//    }

//    @Bean
//    public ProxyFactoryBean userService(){
//        ProxyFactoryBean factoryBean=new ProxyFactoryBean();
//        factoryBean.setTarget(userServiceImpl());
//        factoryBean.setInterceptorNames("transactionAdvisor");
//        return factoryBean;
//    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl(userDao(),mailSender());
    }

//    @Bean
//    public FactoryBean message(){
//        return new MessageFactoryBean("Factory");
//    }

    @Bean
    public UserService testUserService(){
        return new TestUserServiceImpl(userDao(),mailSender(),"3");
    }


}
