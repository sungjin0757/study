package study.querydsl.learning;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class LearningTest {
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("querydsl 실행 검증 테스트")
    void initTest(){
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory q = new JPAQueryFactory(em);
        QHello qHello = new QHello("hello");

        Hello findHello = q
                .selectFrom(qHello)
                .fetchOne();

        Assertions.assertAll(()->{
            Assertions.assertEquals(hello, findHello);
        });
    }
}
