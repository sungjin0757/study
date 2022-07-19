package study.querydsl.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;
import study.querydsl.entity.Team;
import study.querydsl.entity.User;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    User user3;
    User user4;

    @BeforeEach
    void setUp(){
        user3 = new User("user3", 30);
        user4 = new User("user4", 30);
        em.persist(user3);
        em.persist(user4);
    }

    @Test
    @DisplayName("save test")
    void save(){
        User user1 = new User("user1", 10);
        User user2 = new User("user2", 10);
        userRepository.save(user1);
        userRepository.save(user2);

        em.flush();
        em.clear();

        User findUser1 = em.find(User.class, user1.getId());
        User findUser2 = em.find(User.class, user2.getId());

        Assertions.assertAll(() -> {
            org.assertj.core.api.Assertions.assertThat(findUser1).isEqualTo(user1);
            org.assertj.core.api.Assertions.assertThat(findUser2).isEqualTo(user2);
        });
    }

    @Test
    @DisplayName("findById Test")
    void findById(){
        User findUser = userRepository.findById(user3.getId()).get();
        Optional<User> findUser2 = userRepository.findById(-99l);
        Assertions.assertAll(() ->{
           Assertions.assertEquals(findUser, user3);
           Assertions.assertEquals(Optional.empty(), findUser2);
        });
    }

    @Test
    @DisplayName("findAll Test")
    void findAll(){
        List<User> users = userRepository.findAll();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(users.size(), 2);
            org.assertj.core.api.Assertions.assertThat(users)
                    .contains(user3, user4);
        });
    }

    @Test
    @DisplayName("findByUserName Test")
    void findByUserName(){
        List<User> users = userRepository.findByUserName("user3");

        Assertions.assertAll(() -> {
            Assertions.assertEquals(users.size(), 1);
            org.assertj.core.api.Assertions.assertThat(users)
                    .doesNotContain(user4);
        });
    }

    @Test
    @DisplayName("searchByBuilder Test")
    void searchByBuilder(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        User user1 = new User("user1", 10, teamA);
        User user2 = new User("user2", 10, teamB);
        User user3 = new User("user3", 30, teamB);
        User user4 = new User("user4", 40, teamA);

        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        UserSearchCondition condition = new UserSearchCondition();
        condition.setAgeGoe(25);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<UserWithTeamDto> res = userRepository.searchByBuilder(condition);

        Assertions.assertAll(() -> {
            org.assertj.core.api.Assertions.assertThat(res)
                    .extracting("userName")
                    .containsExactly("user3");
        });
    }

    @Test
    @DisplayName("searchByWhere Test")
    void searchByWhere(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        User user1 = new User("user1", 10, teamA);
        User user2 = new User("user2", 10, teamB);
        User user3 = new User("user3", 30, teamB);
        User user4 = new User("user4", 40, teamA);

        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        UserSearchCondition condition = new UserSearchCondition();
        condition.setAgeGoe(25);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<UserWithTeamDto> res = userRepository.searchByWhere(condition);

        Assertions.assertAll(() -> {
            org.assertj.core.api.Assertions.assertThat(res)
                    .extracting("userName")
                    .containsExactly("user3");
        });
    }
}