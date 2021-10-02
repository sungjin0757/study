package study.test.nativeTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.test.domain.User;
import study.test.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class nativeTestV1 {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("V1 테스트")
    void V1_테스트(){

        userService.removeAll();
        Assertions.assertThat(userService.getCountInDb()).isEqualTo(0);

        User user1=createUser("hong","1234");
        Long saveId1 = userService.join(user1);
        Assertions.assertThat(userService.getCountInDb()).isEqualTo(1);

        User user2=createUser("hong1","123");
        Long saveId2 = userService.join(user2);
        Assertions.assertThat(userService.getCountInDb()).isEqualTo(2);

        User user3=createUser("hong12","1233");
        Long saveId3 = userService.join(user3);
        Assertions.assertThat(userService.getCountInDb()).isEqualTo(3);

        User findUser1 = userService.findOne(saveId1);
        Assertions.assertThat(findUser1.getName()).isEqualTo(user1.getName());
        Assertions.assertThat(findUser1.getPassword()).isEqualTo(user1.getPassword());

        User findUser2 = userService.findOne(saveId2);
        Assertions.assertThat(findUser2.getName()).isEqualTo(user2.getName());
        Assertions.assertThat(findUser2.getPassword()).isEqualTo(user2.getPassword());

        User findUser3 = userService.findOne(saveId3);
        Assertions.assertThat(findUser3.getName()).isEqualTo(user3.getName());
        Assertions.assertThat(findUser3.getPassword()).isEqualTo(user3.getPassword());

    }

    private User createUser(String name,String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
