package study.aop.service;

import org.springframework.mail.MailSender;
import org.springframework.transaction.annotation.Transactional;
import study.aop.dao.UserDao;
import study.aop.domain.User;

import java.util.List;

public class TestUserServiceImpl extends UserServiceImpl{
    private final String id;

    public TestUserServiceImpl(UserDao userDao, MailSender mailSender,
                               String id) {
        super(userDao,mailSender);
        this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
        if(user.getId().equals(this.id))
            throw new TestUserServiceException();
        super.upgradeLevel(user);
    }
    static class TestUserServiceException extends RuntimeException{

    }

    @Override
    public List<User> getAll() {
        for(User user:super.getAll()){
            super.update(user);
        }
        return null;
    }
}
