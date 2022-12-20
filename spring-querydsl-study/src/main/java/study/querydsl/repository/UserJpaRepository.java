package study.querydsl.repository;

import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;
import study.querydsl.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository {
    void save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    List<User> findByUserName(String userName);
    List<UserWithTeamDto> searchByBuilder(UserSearchCondition condition);
    List<UserWithTeamDto> searchByWhere(UserSearchCondition condition);
}
