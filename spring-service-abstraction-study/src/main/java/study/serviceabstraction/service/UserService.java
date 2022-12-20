package study.serviceabstraction.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import study.serviceabstraction.dao.Level;
import study.serviceabstraction.dao.UserDao;
import study.serviceabstraction.domain.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /**
     * V1버전 특정 JDBC Connection에 의존하는 상태
     */
//    private final DataSource dataSource;

    private final PlatformTransactionManager transactionManager;
    private final MailSender mailSender;

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

    /**
     * 스프링 트랜잭션 활용
     */
    public void upgradeLevels(){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            List<User> users=userDao.getAll();
            for(User user:users){
                if(checkUpgrade(user)){
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        }catch(Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void add(User user){
        if(user.getLevel()==null)
            user.updateLevel(Level.BASIC);
        userDao.add(user);
    }

    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeMail(user);
    }

    /**
     *
     * 실제 smtp서버 연동. 즉, 네트워크 연결 및 부하가 많은 작업 테스트만을 하기에는 Heavy.
     */
//    private void sendUpgradeMail(User user){
//        Properties props=new Properties();
//        props.put("mail.smtp.host","mail.ksug.org");
//        Session s=Session.getInstance(props,null);
//
//        MimeMessage mimeMessage=new MimeMessage(s);
//        try{
//            mimeMessage.setFrom(new InternetAddress("admin@ksug.org"));
//            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(user.getEmail()));
//            mimeMessage.setSubject("Upgrade 안내");
//            mimeMessage.setText("사용자님의 등급이 "+user.getLevel().name()+"로 업그레이드 되셨습니다.");
//
//            Transport.send(mimeMessage);
//        }catch(AddressException e){
//            throw new RuntimeException(e);
//        }catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void sendUpgradeMail(User user){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("admin");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 "+user.getLevel()+"로 업그레이드 돠었습니다.");

        mailSender.send(mailMessage);
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
