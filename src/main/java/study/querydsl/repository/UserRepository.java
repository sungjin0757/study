package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.querydsl.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, QuerydslPredicateExecutor {
    List<User> findByUserName(String userName);
}
