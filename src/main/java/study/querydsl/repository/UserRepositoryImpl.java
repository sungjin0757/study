package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.QUserWithTeamDto;
import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;
import study.querydsl.entity.User;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.querydsl.entity.QTeam.team;
import static study.querydsl.entity.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserWithTeamDto> search(UserSearchCondition condition) {
        return queryFactory
                .select(new QUserWithTeamDto(user.id, user.userName, user.age,
                        team.id, team.teamName))
                .from(user)
                .leftJoin(user.team, team)
                .where(userNameEq(condition.getUserName()), teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()))
                .fetch();
    }

    @Override
    public Page<UserWithTeamDto> searchPageSim(UserSearchCondition condition, Pageable pageable) {
        QueryResults<UserWithTeamDto> res = queryFactory
                .select(new QUserWithTeamDto(user.id, user.userName, user.age,
                        team.id, team.teamName))
                .from(user)
                .leftJoin(user.team, team)
                .where(userNameEq(condition.getUserName()), teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UserWithTeamDto> content = res.getResults();
        long count = res.getTotal();
        log.info("offset = {}",pageable.getOffset());
        log.info("pageSize = {}",pageable.getPageSize());
        log.info("count = {}",count);
        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<UserWithTeamDto> searchComp(UserSearchCondition condition, Pageable pageable) {
        List<UserWithTeamDto> res = queryFactory
                .select(new QUserWithTeamDto(user.id, user.userName, user.age,
                        team.id, team.teamName))
                .from(user)
                .leftJoin(user.team, team)
                .where(userNameEq(condition.getUserName()), teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<User> countQuery = queryFactory
                .select(user)
                .from(user)
                .leftJoin(user.team, team)
                .where(userNameEq(condition.getUserName()), teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()), ageLoe(condition.getAgeLoe()));

        return PageableExecutionUtils.getPage(res, pageable, countQuery::fetchCount);
//        return new PageImpl<>(res, pageable, count);
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
