package study.aop.service;

import org.springframework.transaction.annotation.Transactional;
import study.aop.domain.User;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {
    @Transactional(readOnly = false)
    void add(User user);

    @Transactional(readOnly = false)
    void upgradeLevels();

    User get(String id);
    List<User> getAll();
    int getCount();

    @Transactional(readOnly = false)
    void deleteAll();

    @Transactional(readOnly = false)
    void update(User user);
}
