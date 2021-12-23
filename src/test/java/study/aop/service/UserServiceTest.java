package study.aop.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import study.aop.configuration.AppConfig;
import study.aop.configuration.bean.TransactionFactoryBean;
import study.aop.domain.Level;
import study.aop.dao.UserDao;
import study.aop.domain.User;
import study.aop.proxy.handler.TransactionHandler;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.*;


@SpringBootTest
@ContextConfiguration(classes= AppConfig.class)
public class UserServiceTest {
    @Autowired
    UserService userService;

//    @Autowired
//    ProxyFactoryBean proxyFactoryBean;
    @Autowired
    UserService testUserService;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

//    @Autowired
//    TransactionFactoryBean transactionFactoryBean;

    @BeforeEach
    void setUp(){
        users= Arrays.asList(
                createUser("1","hong","1234",Level.BASIC, UserServiceImpl.LOG_COUNT_FOR_SILVER-1,0
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now()),
                createUser("2","hong1","1234",Level.BASIC, UserServiceImpl.LOG_COUNT_FOR_SILVER,10
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now()),
                createUser("3","hong12","1234",Level.SILVER,55, UserServiceImpl.REC_COUNT_FOR_GOLD
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now()),
                createUser("4","hong22","1234",Level.GOLD,60, UserServiceImpl.REC_COUNT_FOR_GOLD
                        ,"sungjin0757@naver.com",LocalDateTime.now(),LocalDateTime.now())
        );
    }


    @Test
    @DisplayName("Business Logic Test")
    @DirtiesContext
    void 비즈니스_로직_테스트() throws Exception{
        MockUserDao mockUserDao=new MockUserDao(users);

        MockMailSender mockMailSender=new MockMailSender();
        UserServiceImpl userServiceImpl=new UserServiceImpl(mockUserDao,mockMailSender);

        UserService dynamicProxy=(UserService) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{UserService.class},new TransactionHandler(userServiceImpl,
                        transactionManager,"upgradeLevels"));

        dynamicProxy.upgradeLevels();

        List<User> updatedUser = mockUserDao.getUpdated();
        Assertions.assertThat(updatedUser.size()).isEqualTo(2);
        checkIdAndLevel(updatedUser.get(0),"2",Level.SILVER);
        checkIdAndLevel(updatedUser.get(1),"3",Level.GOLD);

        List<String> request=mockMailSender.getRequests();
        Assertions.assertThat(request.size()).isEqualTo(2);
        Assertions.assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        Assertions.assertThat(request.get(1)).isEqualTo(users.get(2).getEmail());
    }

    @Test
    @DisplayName("Service Add Test")
    void 서비스_add_메서드_테스트(){
        userDao.deleteAll();

        User user1=users.get(0);
        user1.updateLevel(null);

        User user2=users.get(3);

        userService.add(user1);
        userService.add(user2);

        checkLevel(user1, Level.BASIC);
        checkLevel(user2, Level.GOLD);
    }

    @Test
    @DisplayName("Rollback Test")
    @DirtiesContext
    void 롤백_태스트() throws Exception{
//        TestUserService testUserService=new TestUserService(userDao,mailSender,users.get(2).getId());

//        UserServiceTx userServiceTx=new UserServiceTx(testUserService,transactionManager);
//        transactionFactoryBean=new TransactionFactoryBean(testUserService,transactionManager,"upgradeLevels",
//                UserService.class);
//        UserService userServiceTx=(UserService) transactionFactoryBean.getObject();
//        proxyFactoryBean.setTarget(testUserService);
//
//        UserService userServiceTx=(UserService) proxyFactoryBean.getObject();

        userDao.deleteAll();

        for(User user:users){
            userDao.add(user);
        }

        try{
           testUserService.upgradeLevels();
        }catch(TestUserServiceImpl.TestUserServiceException e){
        }

        checkUpdateLevel(users.get(1),false);
    }

    @Test
    @DisplayName("Mockito Test")
    void 목_오브젝트_테스트(){
        UserDao userDao= Mockito.mock(UserDao.class);
        Mockito.when(userDao.getAll()).thenReturn(this.users);

        MailSender mailSender=Mockito.mock(MailSender.class);

        UserServiceImpl userService=new UserServiceImpl(userDao,mailSender);
        userService.upgradeLevels();

        Mockito.verify(userDao,Mockito.times(2)).update(Mockito.any(User.class));
        Mockito.verify(userDao,Mockito.times(1)).getAll();
        Mockito.verify(userDao).update(users.get(1));
        Assertions.assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        Mockito.verify(userDao).update(users.get(2));
        Assertions.assertThat(users.get(2).getLevel()).isEqualTo(Level.GOLD);


        ArgumentCaptor<SimpleMailMessage> mailMessageArg=ArgumentCaptor.forClass(SimpleMailMessage.class);
        //send 가 2번 발생. send 의 파라미터가 SimpleMailMessage임 이걸 ArgumentCaptor에 capture 해두는 것
        Mockito.verify(mailSender,Mockito.times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> request=mailMessageArg.getAllValues();

        Assertions.assertThat(request.size()).isEqualTo(2);
        Assertions.assertThat(request.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        Assertions.assertThat(request.get(1).getTo()[0]).isEqualTo(users.get(2).getEmail());
    }


    public static User createUser(String id, String name, String password,
                                   Level level, int login, int recommend,String email,
                            LocalDateTime createdAt,LocalDateTime lastUpgraded){
        return User.createUser()
                .id(id)
                .name(name)
                .password(password)
                .level(level)
                .login(login)
                .recommend(recommend)
                .email(email)
                .createdAt(createdAt)
                .lastUpgraded(lastUpgraded)
                .build();
    }

    private void checkIdAndLevel(User user,String id,Level expectedValue){
        Assertions.assertThat(user.getId()).isEqualTo(id);
        Assertions.assertThat(user.getLevel()).isEqualTo(expectedValue);
    }

    private void checkLevel(User user,Level level){
        User findUser = userDao.get(user.getId()).orElseThrow(() -> {
            throw new NoSuchElementException();
        });

        Assertions.assertThat(user.getLevel()).isEqualTo(findUser.getLevel());
    }

    private void checkUpdateLevel(User user,boolean upgraded){
        User findUser = userDao.get(user.getId()).orElseThrow(() -> {
            throw new NoSuchElementException();
        });
        if(upgraded){
            Assertions.assertThat(user.getLevel().getNext()).isEqualTo(findUser.getLevel());
        }else if(!upgraded){
            Assertions.assertThat(user.getLevel()).isEqualTo(findUser.getLevel());
        }
    }

    @Getter
    static class MockMailSender implements MailSender{
        private List<String> requests=new ArrayList<>();

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }

    @RequiredArgsConstructor
    @Getter
    static class MockUserDao implements UserDao{
        private final List<User> users;
        private List<User> updated=new ArrayList<>();

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<User> get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }
    }
}
