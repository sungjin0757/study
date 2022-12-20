package study.aop.transaction;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import study.aop.configuration.AppConfig;
import study.aop.dao.UserDao;
import study.aop.domain.Level;
import study.aop.domain.User;
import study.aop.service.UserService;
import study.aop.service.UserServiceImpl;
import study.aop.service.UserServiceTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes= AppConfig.class)
@Transactional
@Slf4j
public class TransactionSyncTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserService userService;

    List<User> users;

    @BeforeEach
    void setUp(){
        users= Arrays.asList(
                UserServiceTest.createUser("1", "hong", "1234", Level.BASIC, UserServiceImpl.LOG_COUNT_FOR_SILVER - 1, 0
                        , "sungjin0757@naver.com", LocalDateTime.now(), LocalDateTime.now()),
                UserServiceTest.createUser("2","hong1","1234",Level.BASIC, UserServiceImpl.LOG_COUNT_FOR_SILVER,10
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now()),
                UserServiceTest.createUser("3","hong12","1234",Level.SILVER,55, UserServiceImpl.REC_COUNT_FOR_GOLD
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now()),
                UserServiceTest.createUser("4","hong22","1234",Level.GOLD,60, UserServiceImpl.REC_COUNT_FOR_GOLD
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now())
        );
    }
    @Test
    @DisplayName("Transaction Sync Test")
    void 트랜잭션_동기화_테스트(){
        //h2에서는 readonly를 true를 하던 false를 하던 적용안됨..
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setReadOnly(true);

        TransactionStatus status = transactionManager.getTransaction(definition);
        userService.deleteAll();

        userService.add(users.get(2));
        userService.add(users.get(3));

        transactionManager.commit(status);
    }

    @Test
    @DisplayName("Transaction Rollback Test")
    void 트랜잭션_롤백_테스트(){
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(definition);

        try{
            userService.deleteAll();
            userService.add(users.get(0));
            userService.add(users.get(1));
            Assertions.assertThat(userService.getCount()).isEqualTo(2);
        }finally {
            transactionManager.rollback(status);
        }

        Assertions.assertThat(userService.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Transaction Rollback Test -2")
    void 트랜잭션_롤백_테스트2(){
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
        Assertions.assertThat(userService.getCount()).isEqualTo(2);
    }

    @AfterTransaction
    void afterTransaction(){
        log.info("After Transaction Execution!");
        Assertions.assertThat(userService.getCount()).isEqualTo(0);
    }

}
