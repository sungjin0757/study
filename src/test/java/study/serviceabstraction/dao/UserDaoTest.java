package study.serviceabstraction.dao;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import study.serviceabstraction.configuration.AppConfig;
import study.serviceabstraction.domain.User;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(classes=AppConfig.class)
@Slf4j
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp(){
        this.user1=createUser("1","hong","1234",Level.BASIC,1,0);
        this.user2=createUser("2","hong1","1234",Level.SILVER,50,10);
        this.user3=createUser("3","hong12","1234",Level.GOLD,55,30);

        log.info("userService = {}",this.userDao);
        log.info("this = {} ",this);
    }

    @Test
    @DisplayName("Enum Test")
    void enum_테스트(){
        log.info("Level={}",Level.BASIC);
        log.info("Level Value={}",Level.BASIC.getValue());
        log.info("Level Type={}",String.valueOf(Level.BASIC).getClass().getName());
    }

    @Test
    @DisplayName("First Test")
    void 실행_테스트() throws Exception{
        userDao.deleteAll();

        Assertions.assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(user1);
        Assertions.assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(user2);
        Assertions.assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(user3);
        Assertions.assertThat(userDao.getCount()).isEqualTo(3);

        User findUser1 = userDao.get("1").get();
        Assertions.assertThat(user1.getId()).isEqualTo(findUser1.getId());
        Assertions.assertThat(user1.getName()).isEqualTo(findUser1.getName());
        Assertions.assertThat(user1.getPassword()).isEqualTo(findUser1.getPassword());
        Assertions.assertThat(user1.getLevel()).isEqualTo(findUser1.getLevel());
        Assertions.assertThat(user1.getLogin()).isEqualTo(findUser1.getLogin());
        Assertions.assertThat(user1.getRecommend()).isEqualTo(findUser1.getRecommend());

        User findUser2 = userDao.get("2").get();
        Assertions.assertThat(user2.getId()).isEqualTo(findUser2.getId());
        Assertions.assertThat(user2.getName()).isEqualTo(findUser2.getName());
        Assertions.assertThat(user2.getPassword()).isEqualTo(findUser2.getPassword());
        Assertions.assertThat(user2.getLevel()).isEqualTo(findUser2.getLevel());
        Assertions.assertThat(user2.getLogin()).isEqualTo(findUser2.getLogin());
        Assertions.assertThat(user2.getRecommend()).isEqualTo(findUser2.getRecommend());

        User findUser3 = userDao.get("3").get();
        Assertions.assertThat(user3.getId()).isEqualTo(findUser3.getId());
        Assertions.assertThat(user3.getName()).isEqualTo(findUser3.getName());
        Assertions.assertThat(user3.getPassword()).isEqualTo(findUser3.getPassword());
        Assertions.assertThat(user3.getLevel()).isEqualTo(findUser3.getLevel());
        Assertions.assertThat(user3.getLogin()).isEqualTo(findUser3.getLogin());
        Assertions.assertThat(user3.getRecommend()).isEqualTo(findUser3.getRecommend());
    }

    @Test
    @DisplayName("First Exception Test")
    void 예외_테스트(){
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            userDao.get("4");
        });
    }

    @Test
    @DisplayName("Duplicate Exception Test")
    void 예외_테스트2(){
        userDao.deleteAll();

        userDao.add(user1);
        Assertions.assertThat(userDao.getCount()).isEqualTo(1);

        Optional<User> findUser = userDao.get("1");
        User user = findUser.stream().findFirst().orElse(null);
        Assertions.assertThat(user.getId()).isEqualTo("1");
        Assertions.assertThat(user.getName()).isEqualTo("hong");
        Assertions.assertThat(user.getPassword()).isEqualTo("1234");

        org.junit.jupiter.api.Assertions.assertThrows(DuplicateKeyException.class,()->{
            userDao.add(user1);
        });
    }

    @Test
    @DisplayName("All User Test")
    void 모든유저불러오기_테스트(){
        userDao.deleteAll();

        List<User> findAllUser= userDao.getAll();

        Assertions.assertThat(findAllUser.size()).isEqualTo(0);

        userDao.add(user1);
        Assertions.assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(user2);
        Assertions.assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(user3);
        Assertions.assertThat(userDao.getCount()).isEqualTo(3);

        findAllUser= userDao.getAll();

        Assertions.assertThat(findAllUser.size()).isEqualTo(3);
        checkUser(user1,findAllUser.get(0));
        checkUser(user2,findAllUser.get(1));
        checkUser(user3,findAllUser.get(2));

    }

    @Test
    @DisplayName("Update Test")
    void 수정_테스트(){
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.updateLevel(Level.GOLD);
        user1.updateLogin(50);
        user1.updateRecommend(30);
        userDao.update(user1);

        User findUser1=userDao.get(user1.getId()).get();
        checkUser(user1,findUser1);

        User findUser2 = userDao.get(user2.getId()).get();
        checkUser(user2,findUser2);
    }

    private void checkUser(User user1,User user2){
        Assertions.assertThat(user1.getId()).isEqualTo(user2.getId());
        Assertions.assertThat(user1.getName()).isEqualTo(user2.getName());
        Assertions.assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        Assertions.assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        Assertions.assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        Assertions.assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    private User createUser(String id,String name,String password,Level level,int login,int recommend){
        return User.createUser()
                .id(id)
                .name(name)
                .password(password)
                .level(level)
                .login(login)
                .recommend(recommend)
                .build();
    }
}
