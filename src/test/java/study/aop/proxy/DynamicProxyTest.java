package study.aop.proxy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import study.aop.dao.UserDao;
import study.aop.domain.Level;
import study.aop.domain.User;
import study.aop.proxy.handler.TransactionHandler;
import study.aop.proxy.handler.UpperClassHandler;
import study.aop.service.UserService;
import study.aop.service.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class DynamicProxyTest {

    @Test
    @DisplayName("Dynamic Proxy Test - UpperClass")
    void 다이나믹_프록시_테스트_대문자(){

        Hello dynamicProxy= (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{Hello.class}, new UpperClassHandler(new HelloTarget()));

        Assertions.assertThat(dynamicProxy.welcome("h")).isEqualTo("h Welcome".toUpperCase());
        Assertions.assertThat(dynamicProxy.goodBye("h")).isEqualTo("h Good Bye".toUpperCase());
    }


}
