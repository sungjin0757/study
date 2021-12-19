package study.aop.learning;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import study.aop.configuration.AppConfig;
import study.aop.dao.UserDao;
import study.aop.domain.Level;
import study.aop.domain.User;
import study.aop.service.UserService;
import study.aop.service.UserServiceImpl;
import study.aop.service.UserServiceTest;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes=AppConfig.class)
public class LearningTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Test
    @DisplayName("Reflect Method API Test")
    void 메소드_api_테스트() throws Exception{
        String name="Hong";
        Assertions.assertThat(name.length()).isEqualTo(4);

        Method lengthMethod=String.class.getMethod("length");
        Assertions.assertThat(lengthMethod.invoke(name)).isEqualTo(4);

        Assertions.assertThat(name.charAt(2)).isEqualTo('n');

        Method charAtMethod=String.class.getMethod("charAt", int.class);
        Assertions.assertThat(charAtMethod.invoke(name,2)).isEqualTo('n');
    }

    @Test
    @DisplayName("DI Test - User Service")
    void 유저_서비스_주입_테스트(){
        userDao.deleteAll();

        userService.add(UserServiceTest.createUser("1","hong","1234", Level.BASIC,
                UserServiceImpl.LOG_COUNT_FOR_SILVER-1,0
                ,"sungjin0757@naver.com", LocalDateTime.now(),LocalDateTime.now()));
        userService.add(UserServiceTest.createUser("2","hong","12345",Level.BASIC,
                UserServiceImpl.LOG_COUNT_FOR_SILVER-1,0
                ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now()));

        log.info("{}",userService.getClass());

        Assertions.assertThat(userDao.getCount()).isEqualTo(2);
        Assertions.assertThat(userDao.get("1").get().getPassword()).isEqualTo("1234");
        Assertions.assertThat(userDao.get("2").get().getPassword()).isEqualTo("12345");
    }

}
