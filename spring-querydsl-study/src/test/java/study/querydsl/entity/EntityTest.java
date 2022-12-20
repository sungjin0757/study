package study.querydsl.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EntityTest {
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("데이터 저장 테스트")
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        User user1 = new User("user1", 10, teamA);
        User user2 = new User("user2", 20, teamB);
        User user3 = new User("user3", 30, teamB);
        User user4 = new User("user4", 40, teamA);

        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        em.flush();
        em.clear();

        List<User> users = em.createQuery("select u from User u", User.class)
                .getResultList();

        Set<Team> teams = new HashSet<>();
        for(User user : users){
            teams.add(user.getTeam());
        }

        Assertions.assertAll(()->{
            for(User user : users){
                Assertions.assertNotNull(user.getCreatedDate());
                Assertions.assertNotNull(user.getUpdatedDate());
            }
            org.assertj.core.api.Assertions.assertThat(users).containsExactly(user1, user2, user3, user4);
            org.assertj.core.api.Assertions.assertThat(teams).containsExactly(teamA, teamB);
        });
    }

}