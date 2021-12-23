package study.aop.service;

import study.aop.domain.User;

import java.util.List;

public interface UserService {
    void add(User user);
    void upgradeLevels();
    User get(String id);
    List<User> getAll();
    int getCount();
    void deleteAll();
    void update(User user);
}
