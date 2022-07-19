package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.QUserWithTeamDto;
import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.QUser;
import study.querydsl.entity.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.*;
import static study.querydsl.entity.QTeam.*;
import static study.querydsl.entity.QUser.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryByQueryDsl implements UserRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return queryFactory
                .selectFrom(user)
                .fetch();
    }

    @Override
    public List<User> findByUserName(String userName) {
        return queryFactory
                .selectFrom(user)
                .where(user.userName.eq(userName))
                .fetch();
    }

    @Override
    public List<UserWithTeamDto> searchByBuilder(UserSearchCondition condition) {
        BooleanBuilder builder =new BooleanBuilder();
        if (hasText(condition.getUserName())) {
            builder.and(user.userName.eq(condition.getUserName()));
        }
        if(hasText(condition.getTeamName())){
            builder.and(team.teamName.eq(condition.getTeamName()));
        }
        if(condition.getAgeGoe() != null){
            builder.and(user.age.goe(condition.getAgeGoe()));
        }
        if(condition.getAgeLoe() != null){
            builder.and(user.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QUserWithTeamDto(user.id, user.userName, user.age,
                        team.id, team.teamName))
                .from(user)
                .leftJoin(user.team, team)
                .where(builder)
                .fetch();
    }

    @Override
    public List<UserWithTeamDto> searchByWhere(UserSearchCondition condition) {
        return queryFactory
                .select(new QUserWithTeamDto(user.id, user.userName, user.age,
                        team.id, team.teamName))
                .from(user)
                .leftJoin(user.team, team)
                .where(userNameEq(condition.getUserName()), teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()))
                .fetch();
    }

    private BooleanExpression userNameEq(String userNameParam){
        return !hasText(userNameParam) ? null : user.userName.eq(userNameParam);
    }

    private BooleanExpression teamNameEq(String teamNameParam){
        return !hasText(teamNameParam) ? null : team.teamName.eq(teamNameParam);
    }

    private BooleanExpression ageGoe(Integer ageGoe){
        return ageGoe == null ? null : user.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe){
        return ageLoe == null ? null : user.age.loe(ageLoe);
    }

}
