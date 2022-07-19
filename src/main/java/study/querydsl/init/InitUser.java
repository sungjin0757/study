package study.querydsl.init;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Team;
import study.querydsl.entity.User;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitUser {
    private final InitUserService initUserService;

    @PostConstruct
    void init(){
        initUserService.init();
    }

    @Component
    static class InitUserService{
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init(){
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100 ; i++){
                Team team = i % 2 == 0 ? teamA : teamB;
                em.persist(new User("user"+i, i, team));
            }
        }
    }
}
