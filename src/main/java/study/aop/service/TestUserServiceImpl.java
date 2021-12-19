package study.aop.service;

import org.springframework.mail.MailSender;
import study.aop.dao.UserDao;
import study.aop.domain.User;

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
}
