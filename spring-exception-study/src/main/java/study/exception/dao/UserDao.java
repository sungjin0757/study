package study.exception.dao;

import study.exception.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void add(User user);
    Optional<User> get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
