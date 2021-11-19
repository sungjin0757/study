package study.exception.dao;

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
import study.exception.configuration.AppConfig;
import study.exception.domain.User;
import study.exception.exception.DuplicateUserException;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(classes=AppConfig.class)
@Slf4j
public class UserDaoV1Test {

    @Autowired
    UserDao userDao;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp(){
        this.user1=createUser("1","hong","1234");
        this.user2=createUser("2","hong1","1234");
        this.user3=createUser("3","hong12","1234");

        log.info("userService = {}",this.userDao);
        log.info("this = {} ",this);
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

        User findUser2 = userDao.get("2").get();
        Assertions.assertThat(user2.getId()).isEqualTo(findUser2.getId());
        Assertions.assertThat(user2.getName()).isEqualTo(findUser2.getName());
        Assertions.assertThat(user2.getPassword()).isEqualTo(findUser2.getPassword());

        User findUser3 = userDao.get("3").get();
        Assertions.assertThat(user3.getId()).isEqualTo(findUser3.getId());
        Assertions.assertThat(user3.getName()).isEqualTo(findUser3.getName());
        Assertions.assertThat(user3.getPassword()).isEqualTo(findUser3.getPassword());
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
    private void checkUser(User user1,User user2){
        Assertions.assertThat(user1.getId()).isEqualTo(user2.getId());
        Assertions.assertThat(user1.getName()).isEqualTo(user2.getName());
        Assertions.assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }

    private User createUser(String id,String name,String password){
        return User.createUser()
                .id(id)
                .name(name)
                .password(password)
                .build();
    }
}
