package study.template.dao;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import study.template.configuration.AppConfig;
import study.template.domain.User;

@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
@Slf4j
public class UserDaoV1V1Test {

    @Autowired
    UserDaoV1 userDaoV1;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp(){
        this.user1=createUser("1","hong","1234");
        this.user2=createUser("2","hong1","1234");
        this.user3=createUser("3","hong12","1234");

        log.info("userService = {}",this.userDaoV1);
        log.info("this = {} ",this);
    }

    @Test
    @DisplayName("First Test")
    void 실행_테스트() throws Exception{
        userDaoV1.deleteAll();

        Assertions.assertThat(userDaoV1.getCount()).isEqualTo(0);

        userDaoV1.add(user1);
        Assertions.assertThat(userDaoV1.getCount()).isEqualTo(1);

        userDaoV1.add(user2);
        Assertions.assertThat(userDaoV1.getCount()).isEqualTo(2);

        userDaoV1.add(user3);
        Assertions.assertThat(userDaoV1.getCount()).isEqualTo(3);

        User findUser1 = userDaoV1.get("1");
        Assertions.assertThat(user1.getId()).isEqualTo(findUser1.getId());
        Assertions.assertThat(user1.getName()).isEqualTo(findUser1.getName());
        Assertions.assertThat(user1.getPassword()).isEqualTo(findUser1.getPassword());

        User findUser2 = userDaoV1.get("2");
        Assertions.assertThat(user2.getId()).isEqualTo(findUser2.getId());
        Assertions.assertThat(user2.getName()).isEqualTo(findUser2.getName());
        Assertions.assertThat(user2.getPassword()).isEqualTo(findUser2.getPassword());

        User findUser3 = userDaoV1.get("3");
        Assertions.assertThat(user3.getId()).isEqualTo(findUser3.getId());
        Assertions.assertThat(user3.getName()).isEqualTo(findUser3.getName());
        Assertions.assertThat(user3.getPassword()).isEqualTo(findUser3.getPassword());
    }

    @Test
    @DisplayName("First Exception Test")
    void 예외_테스트(){
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            userDaoV1.get("4");
        });
    }

    private User createUser(String id,String name,String password){
        return User.createUser()
                .id(id)
                .name(name)
                .password(password)
                .build();
    }
}
