package study.serviceabstraction.service;

import lombok.RequiredArgsConstructor;
import study.serviceabstraction.dao.Level;
import study.serviceabstraction.dao.UserDao;
import study.serviceabstraction.domain.User;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user:users){
            if(checkUpgrade(user)){
                user.upgradeLevel();
                userDao.update(user);
            }
        }
    }

    public void add(User user){
        if(user.getLevel()==null)
            user.updateLevel(Level.BASIC);
        userDao.add(user);
    }

    private boolean checkUpgrade(User user){
        Level currentLevel=user.getLevel();

        if(currentLevel==Level.BASIC)
            return (user.getLogin()>=50);
        else if(currentLevel==Level.SILVER)
            return (user.getRecommend()>=30);
        else if(currentLevel==Level.GOLD)
            return false;
        throw new IllegalArgumentException("Unknown Level : "+currentLevel);
    }
}
