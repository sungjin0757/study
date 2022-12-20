package basic.chunkorientedtasklet.common;

import basic.chunkorientedtasklet.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitUserService initUserService;

    @PostConstruct
    void init() {
        initUserService.init();
    }

    @Component
    static class InitUserService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            for (int i = 0; i < 100; i++) {
                em.persist(new User("user" + i, i));
            }
        }
    }
}
