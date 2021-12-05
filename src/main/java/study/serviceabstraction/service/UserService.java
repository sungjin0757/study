package study.serviceabstraction.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import study.serviceabstraction.dao.Level;
import study.serviceabstraction.dao.UserDao;
import study.serviceabstraction.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /**
     * V1버전 특정 JDBC Connection에 의존하는 상태
     */
//    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;

    public static final int LOG_COUNT_FOR_SILVER=50;
    public static final int REC_COUNT_FOR_GOLD=30;

    /**
     *
     * Transaction이 없는 상태
     */
//    public void upgradeLevels(){
//        List<User> users = userDao.getAll();
//        for(User user:users){
//            if(checkUpgrade(user)){
//                upgradeLevel(user);
//            }
//        }
//    }

    /**
     * V1
     * 특정 JDBC Connection에 의존하고 있는 상태
     */
//    public void upgradeLevels() throws Exception{
//        TransactionSynchronizationManager.initSynchronization();
//        Connection c= DataSourceUtils.getConnection(dataSource);
//        c.setAutoCommit(false);
//
//        try{
//            List<User> users = userDao.getAll();
//            for(User user:users){
//                if(checkUpgrade(user)){
//                    upgradeLevel(user);
//                }
//            }
//            c.commit();
//        }catch(Exception e){
//            c.rollback();
//            throw e;
//        }finally {
//            DataSourceUtils.releaseConnection(c,dataSource);
//            TransactionSynchronizationManager.unbindResource(this.dataSource);
//            TransactionSynchronizationManager.clearSynchronization();
//        }
//
//    }

    public void add(User user){
        if(user.getLevel()==null)
            user.updateLevel(Level.BASIC);
        userDao.add(user);
    }

    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean checkUpgrade(User user){
        Level currentLevel=user.getLevel();

        if(currentLevel==Level.BASIC)
            return (user.getLogin()>=LOG_COUNT_FOR_SILVER);
        else if(currentLevel==Level.SILVER)
            return (user.getRecommend()>=REC_COUNT_FOR_GOLD);
        else if(currentLevel==Level.GOLD)
            return false;
        throw new IllegalArgumentException("Unknown Level : "+currentLevel);
    }
}
