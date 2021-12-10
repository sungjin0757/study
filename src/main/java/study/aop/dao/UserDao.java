package study.aop.dao;

import study.aop.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void add(User user);
    Optional<User> get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}
