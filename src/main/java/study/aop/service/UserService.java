package study.aop.service;

import study.aop.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
