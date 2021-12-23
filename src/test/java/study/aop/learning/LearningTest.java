package study.aop.learning;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import study.aop.configuration.AppConfig;
import study.aop.dao.UserDao;
import study.aop.domain.Level;
import study.aop.proxy.pointcut.Bean;
import study.aop.proxy.pointcut.Target;
import study.aop.service.TestUserServiceImpl;
import study.aop.service.UserService;
import study.aop.service.UserServiceImpl;
import study.aop.service.UserServiceTest;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
@ContextConfiguration(classes=AppConfig.class)
public class LearningTest {

    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

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

    @Test
    @DisplayName("PointCut Expression Test")
    void 포인트컷_표현식_테스트() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int " +
                "study.aop.proxy.pointcut.Target.minus(int, int) throws java.lang.RuntimeException)");

        Assertions.assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("minus",int.class,int.class),
                        null)).isEqualTo(true);

        Assertions.assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("plus",int.class,int.class),
                        null)).isEqualTo(false);

        Assertions.assertThat(pointcut.getClassFilter().matches(Bean.class)&&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class),
                        null)).isEqualTo(false);
    }

    @Test
    @DisplayName("PointCut Expression Test - Extended")
    void 포인트컷_표현식_테스트_확장() throws Exception{
        methodMatch("execution(* *(..))",true,true,true,true,true,true);
    }

    @Test
    @DisplayName("Transaction - ReadOnlyTest")
    void 트랜잭션_리드온리_테스트(){
        testUserService.getAll();
    }

    private void pointcutMatch(String exp, Boolean expected,Class<?> clazz,String methodName, Class<?>... args)
        throws Exception{
        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
        pointcut.setExpression(exp);

        Assertions.assertThat(pointcut.getClassFilter().matches(clazz)&&
                pointcut.getMethodMatcher().matches(clazz.getMethod(methodName,args),null))
                .isEqualTo(expected);
    }

    private void methodMatch(String exp,boolean... expected) throws Exception{
        pointcutMatch(exp,expected[0],Target.class,"hello");
        pointcutMatch(exp,expected[1],Target.class,"hello",String.class);
        pointcutMatch(exp,expected[2],Target.class,"minus",int.class,int.class);
        pointcutMatch(exp,expected[3],Target.class,"plus",int.class,int.class);
        pointcutMatch(exp,expected[4],Target.class,"method");
        pointcutMatch(exp,expected[5],Bean.class,"method");
    }
}
