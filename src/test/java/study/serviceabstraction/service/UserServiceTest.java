package study.serviceabstraction.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import study.serviceabstraction.configuration.AppConfig;
import study.serviceabstraction.dao.Level;
import study.serviceabstraction.dao.UserDao;
import study.serviceabstraction.domain.User;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@SpringBootTest
@ContextConfiguration(classes= AppConfig.class)
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    List<User> users;

    @BeforeEach
    void setUp(){
        users= Arrays.asList(
                createUser("1","hong","1234",Level.BASIC,UserService.LOG_COUNT_FOR_SILVER-1,0
                        ,LocalDateTime.now(),LocalDateTime.now()),
                createUser("2","hong1","1234",Level.BASIC, UserService.LOG_COUNT_FOR_SILVER,10
                        ,LocalDateTime.now(),LocalDateTime.now()),
                createUser("3","hong12","1234",Level.SILVER,55,UserService.REC_COUNT_FOR_GOLD
                        ,LocalDateTime.now(),LocalDateTime.now()),
                createUser("4","hong22","1234",Level.GOLD,60,UserService.REC_COUNT_FOR_GOLD
                        ,LocalDateTime.now(),LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("Bean DI Test")
    void 빈_주입_테스트(){
        Assertions.assertThat(this.userService).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Business Logic Test")
    void 비즈니스_로직_테스트() throws Exception{
        userDao.deleteAll();

        for(User user:this.users){
            userService.add(user);
        }

        userService.upgradeLevels();

        checkUpdateLevel(users.get(0),false);
        checkUpdateLevel(users.get(1),true);
        checkUpdateLevel(users.get(2),true);
        checkUpdateLevel(users.get(3),false);
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
    void 롤백_태스트() throws Exception{
        UserService testUserService=new TestUserService(userDao,dataSource,users.get(2).getId());

        userDao.deleteAll();

        for(User user:users){
            userDao.add(user);
        }

        try{
           testUserService.upgradeLevels();
        }catch(TestUserServiceException e){

        }

        checkUpdateLevel(users.get(1),false);
    }

    private User createUser(String id, String name, String password, Level level, int login, int recommend,
                            LocalDateTime createdAt,LocalDateTime lastUpgraded){
        return User.createUser()
                .id(id)
                .name(name)
                .password(password)
                .level(level)
                .login(login)
                .recommend(recommend)
                .createdAt(createdAt)
                .lastUpgraded(lastUpgraded)
                .build();
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

    static class TestUserService extends UserService{
        private final String id;

        public TestUserService(UserDao userDao, DataSource dataSource, String id) {
            super(userDao,dataSource);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{

    }
}
