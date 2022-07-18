package study.querydsl.learning;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.QUserDto;
import study.querydsl.dto.UserDto;
import study.querydsl.dto.UserDto2;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.QUser;
import study.querydsl.entity.Team;
import study.querydsl.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static study.querydsl.entity.QTeam.*;
import static study.querydsl.entity.QUser.*;

@SpringBootTest
@Transactional
public class QueryDslLearningTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp(){
        queryFactory = new JPAQueryFactory(em);

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
    }

    @Test
    @DisplayName("JPQL 테스트")
    void startJPQL(){
        User findUser = em.createQuery("select u from User u where u.userName = :userName", User.class)
                .setParameter("userName", "user1")
                .getSingleResult();

        Assertions.assertAll(()->{
            Assertions.assertEquals(findUser.getUserName(), "user1");
        });
    }

    @Test
    @DisplayName("QueryDsl 테스트")
    void startQueryDsl(){
        User findUser = queryFactory
                .select(user)
                .from(user)
                .where(user.userName.eq("user1"))
                .fetchOne();

        Assertions.assertAll(()->{
            Assertions.assertEquals(findUser.getUserName(), "user1");
        });
    }

    @Test
    @DisplayName("Search 테스트")
    void search(){
        User findUser = queryFactory
                .selectFrom(user)
                .where(user.userName.eq("user2")
                        .and(user.age.eq(20)))
                .fetchOne();

        Assertions.assertAll(()->{
            Assertions.assertEquals(findUser.getUserName(), "user2");
        });
    }

    @Test
    @DisplayName("결과 조회 테스트")
    void fetchTest(){
        List<User> findUsers = queryFactory
                .selectFrom(user)
                .fetch();

        User findUser = queryFactory
                .selectFrom(user)
                .fetchOne();

        User findUser2 = queryFactory
                .selectFrom(user)
                .fetchFirst();

        QueryResults<User> results = queryFactory
                .selectFrom(user)
                .fetchResults();

        long total = results.getTotal();
        List<User> content = results.getResults();

        long count = queryFactory
                .selectFrom(user)
                .fetchCount();

    }

    @Test
    @DisplayName("Sort 테스트")
    void sort(){
        List<User> findUsers = queryFactory
                .selectFrom(user)
                .orderBy(user.age.desc(), user.userName.asc().nullsLast())
                .fetch();

        User findUser4 = findUsers.get(0);
        User findUser3 = findUsers.get(1);
        User findUser1 = findUsers.get(2);
        User findUser2 = findUsers.get(3);

        Assertions.assertAll(()->{
           Assertions.assertEquals(findUser1.getUserName(), "user1");
           Assertions.assertEquals(findUser2.getUserName(), "user2");
           Assertions.assertEquals(findUser3.getUserName(), "user3");
           Assertions.assertEquals(findUser4.getUserName(), "user4");
        });
    }

    @Test
    @DisplayName("Paging 테스트")
    void paging(){
        List<User> findUsers = queryFactory
                .selectFrom(user)
                .orderBy(user.userName.desc())
                .offset(1)
                .limit(2)
                .fetch();

        QueryResults<User> results = queryFactory
                .selectFrom(user)
                .orderBy(user.userName.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        Assertions.assertAll(()->{
            Assertions.assertEquals(findUsers.size(), 2);
            Assertions.assertEquals(results.getTotal(), 4);
            Assertions.assertEquals(results.getLimit(),2);
            Assertions.assertEquals(results.getOffset(),1);
            Assertions.assertEquals(results.getResults().size(),2);
        });
    }

    @Test
    @DisplayName("Set 테스트")
    void set(){
        Tuple result = queryFactory
                .select(user.count(),
                        user.age.sum(),
                        user.age.max(),
                        user.age.min(),
                        user.age.avg())
                .from(user)
                .fetchOne();


        Assertions.assertAll(()->{
            Assertions.assertEquals(result.get(user.count()), 4);
            Assertions.assertEquals(result.get(user.age.sum()), 90);
            Assertions.assertEquals(result.get(user.age.min()), 10);
            Assertions.assertEquals(result.get(user.age.max()), 40);
            Assertions.assertEquals(result.get(user.age.avg()), 90.0/4.0);
        });

    }

    @Test
    @DisplayName("Set 테스트2")
    void set2(){
        List<Tuple> fetch = queryFactory
                .select(team.teamName, user.age.avg())
                .from(user)
                .join(user.team, team)
                .groupBy(team.teamName)
                .fetch();

        Tuple teamA = fetch.get(0);
        Tuple teamB = fetch.get(1);

        Assertions.assertAll(()->{
            Assertions.assertEquals(teamA.get(team.teamName), "teamA");
            Assertions.assertEquals(teamA.get(user.age.avg()), 25);

            Assertions.assertEquals(teamB.get(team.teamName), "teamB");
            Assertions.assertEquals(teamB.get(user.age.avg()), 20);
        });
    }

    @Test
    @DisplayName("Join 테스트")
    void join(){
        List<User> findUsers = queryFactory
                .selectFrom(user)
                .join(user.team, team)
                .where(team.teamName.eq("teamA"))
                .fetch();

        Assertions.assertAll(()->{
            org.assertj.core.api.Assertions.assertThat(findUsers)
                    .extracting("userName")
                    .containsExactly("user1", "user4");
        });
    }

    @Test
    @DisplayName("Join On 테스트")
    void join_on(){
        List<User> findUsers = queryFactory
                .selectFrom(user)
                .join(user.team, team)
                .on(team.teamName.eq("teamB"))
                .fetch();

        List<Tuple> result = queryFactory
                .select(user, team)
                .from(user)
                .leftJoin(user.team, team)
                .on(team.teamName.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println(tuple);
        }

        Assertions.assertAll(() -> {
            org.assertj.core.api.Assertions.assertThat(findUsers)
                    .extracting("userName")
                    .containsExactly("user2", "user3");
        });
    }

    @Test
    @DisplayName("join on without relation 테스트")
    void join_on_without_relation(){
        em.persist(new User("teamA"));
        em.persist(new User("teamB"));
        em.persist(new User("teamC"));

        List<Tuple> result = queryFactory
                .select(user, team)
                .from(user)
                .leftJoin(team)
                .on(user.userName.eq(team.teamName))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    @DisplayName("fetch join no 테스트")
    void fetch_join_no(){
        em.flush();
        em.clear();

        User user1 = queryFactory
                .selectFrom(user)
                .where(user.userName.eq("user1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(user1.getTeam());
        Assertions.assertEquals(loaded, false);
    }

    @Test
    @DisplayName("fetch join 테스트")
    void fetch_join(){
        em.flush();
        em.clear();

        User user1 = queryFactory
                .selectFrom(user)
                .join(user.team, team).fetchJoin()
                .where(user.userName.eq("user1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(user1.getTeam());
        Assertions.assertEquals(loaded, true);
    }

    @Test
    @DisplayName("sub query 테스트")
    void sub_query(){
        QUser userSub = new QUser("userSub");

        User findUser = queryFactory
                .selectFrom(QUser.user)
                .where(user.age.eq(
                        JPAExpressions.select(userSub.age.max())
                                .from(userSub)
                ))
                .fetchOne();

        List<User> result = queryFactory
                .selectFrom(user)
                .where(user.age.goe(
                        JPAExpressions
                                .select(userSub.age.avg())
                                .from(userSub)
                ))
                .fetch();

        Assertions.assertAll(() -> {
            Assertions.assertEquals(findUser.getAge(), 40);
            Assertions.assertEquals(result.size(), 2);
        });
    }

    @Test
    @DisplayName("case 테스트")
    void basic_case(){
        List<String> res = queryFactory
                .select(user.age
                        .when(10).then("열살")
                        .when(20).then("스물")
                        .otherwise("기타"))
                .from(user)
                .fetch();

        /**
         * 복잡할 경우 CaseBuilder 사용
         * new CaseBuilder(). chaining으로 이어나가면 됨.
         */

        for (String re : res) {
            System.out.println(re);
        }
    }

    @Test
    @DisplayName("Concat 테스트")
    void constant(){
        List<String> res = queryFactory
                .select(user.userName.concat("_").concat(user.age.stringValue()))
                .from(user)
                .fetch();

        for (String re : res) {
            System.out.println(re);
        }
    }

    @Test
    @DisplayName("Projection 테스트")
    void projection(){
        List<Integer> userAge = queryFactory
                .select(user.age)
                .from(user)
                .fetch();

        List<Tuple> fetch = queryFactory
                .select(user.userName, user.age)
                .from(user)
                .fetch();

        for (Integer integer : userAge) {
            System.out.println(integer);
        }

        for (Tuple tuple : fetch) {
            String name = tuple.get(user.userName);
            Integer age = tuple.get(user.age);

            System.out.println(name+ ", "+age);
        }
    }

    @Test
    @DisplayName("find Dto (JPQL) 테스트")
    void findDtoByJpql(){
        List<UserDto> resultList = em.createQuery("select " +
                "new study.querydsl.dto.UserDto(u.userName, u.age) " +
                "from User u", UserDto.class).getResultList();

        for (UserDto userDto : resultList) {
            System.out.println(userDto);
        }
    }

    @Test
    @DisplayName("find Dto (QueryDsl) 테스트")
    void findDtoByQueryDsl(){
        // setter
        List<UserDto> res1 = queryFactory
                .select(Projections.bean(UserDto.class, user.userName, user.age))
                .from(user)
                .fetch();

        // 필드 (getter, setter 무시), private 이어도 상관 없이
        List<UserDto> res2 = queryFactory
                .select(Projections.fields(UserDto.class, user.userName, user.age))
                .from(user)
                .fetch();

        // 생성자
        List<UserDto> res3 = queryFactory
                .select(Projections.constructor(UserDto.class, user.userName, user.age))
                .from(user)
                .fetch();

        List<UserDto2> res4 = queryFactory
                .select(Projections.fields(UserDto2.class,
                        user.userName.as("name"), user.age.as("ag")))
                .from(user)
                .fetch();

        QUser userSub = new QUser("userSub");

        List<UserDto2> res5 = queryFactory
                .select(Projections.fields(UserDto2.class,
                        user.userName.as("name"),

                        ExpressionUtils.as(JPAExpressions.select(userSub.age.max())
                                .from(userSub), "ag")))
                .from(user)
                .fetch();

        // 생성자는 타입만 맞으면 됨.
        List<UserDto2> res6 = queryFactory
                .select(Projections.constructor(UserDto2.class,
                        user.userName, user.age))
                .from(user)
                .fetch();

        for (UserDto re : res1) {
            System.out.println(re);
        }

        for (UserDto userDto : res2) {
            System.out.println(userDto);
        }

        for (UserDto userDto : res3) {
            System.out.println(userDto);
        }

        for (UserDto2 userDto2 : res4) {
            System.out.println(userDto2);
        }

        for (UserDto2 userDto2 : res5) {
            System.out.println(userDto2);
        }

        for (UserDto2 userDto2 : res6) {
            System.out.println(userDto2);
        }
    }

    @Test
    @DisplayName("QueryProjection 테스트")
    void queryProjection(){
        List<UserDto> res1 = queryFactory
                .select(new QUserDto(user.userName, user.age))
                .from(user)
                .fetch();

        for (UserDto userDto : res1) {
            System.out.println(userDto);
        }
    }
}
