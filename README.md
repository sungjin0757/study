## AOP - Aspect Oriented Programming
***

### 🔍 유저 서비스기능에 Transaction 경계 설정 기능을 부여하면서, 단계별로 AOP의 등장 배경과 장점을 알아봅시다!

**📌 먼저, 다음의 코드를 기반으로 점차적으로 발전시켜 나가보겠습니다.** 

**User.Java(Domain)**
```java
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {

    private String id;
    private String name;
    private String password;

    private Level level;
    private int login;
    private int recommend;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpgraded;

    public void updateLevel(Level level){
        this.level=level;
    }

    public void updateLogin(int login){
        this.login=login;
    }

    public void updateRecommend(int recommend){
        this.recommend=recommend;
    }

    public void upgradeLevel(){
        Level next=this.level.getNext();
        if(next==null){
            throw new IllegalStateException(this.level+"은 현재 업그레이드가 불가능합니다.");
        }
        updateLevel(next);

        this.lastUpgraded=LocalDateTime.now();
    }

    @Builder(builderMethodName = "createUser")
    public User(String id, String name, String password,Level level,int login,int recommend, String email
            ,LocalDateTime createdAt,LocalDateTime lastUpgraded){
        this.id=id;
        this.name=name;
        this.password=password;
        this.level=level;
        this.login=login;
        this.recommend=recommend;
        this.email=email;
        this.createdAt=createdAt;
        this.lastUpgraded=lastUpgraded;
    }
}

```

**UserDao.java**
```java
public interface UserDao {
    void add(User user);
    Optional<User> get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}
```

**UserDaoImpl.java**
```java
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcOperations jdbcOperations;

    private RowMapper<User> userMapper=new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.createUser()
                    .id(rs.getString("id"))
                    .password(rs.getString("password"))
                    .name(rs.getString("name"))
                    .level(Level.valueOf(rs.getInt("level")))
                    .login(rs.getInt("login"))
                    .recommend(rs.getInt("recommend"))
                    .email(rs.getString("email"))
                    .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                    .lastUpgraded(rs.getTimestamp("lastUpgraded").toLocalDateTime())
                    .build();
        }
    };

    @Override
    public void add(User user) {
        jdbcOperations.update("insert into users(id,name,password,level,login,recommend,email,createdAt,lastUpgraded) " +
                        "values(?,?,?,?,?,?,?,?,?)",
                user.getId(),user.getName(),user.getPassword()
                ,user.getLevel().getValue(),user.getLogin(),user.getRecommend(), user.getEmail()
                , Timestamp.valueOf(user.getCreatedAt()),Timestamp.valueOf(user.getLastUpgraded()));
    }

    @Override
    public Optional<User> get(String id) {
        return Optional.ofNullable(
                jdbcOperations.queryForObject("select * from users u where u.id=?", userMapper,new Object[]{id}));
    }

    @Override
    public List<User> getAll() {
        return jdbcOperations.query("select * from users u order by id", userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcOperations.update("delete from users");
    }

    @Override
    public int getCount() {
        return jdbcOperations.queryForObject("select count(*) from users",Integer.class);
    }

    @Override
    public void update(User user) {
        jdbcOperations.update("update users set name=?,password=?,level=?,login=?,recommend=?,email=?, createdAt=?," +
                        "lastUpgraded=? where id=?",
                user.getName(), user.getPassword(),user.getLevel().getValue()
                ,user.getLogin(),user.getRecommend(),user.getEmail(),user.getCreatedAt(),user.getLastUpgraded(),user.getId());
    }
}

```

**UserService.java**
```java
public interface UserService {
    void add(User user);
    
    void upgradeLevels();

    User get(String id);
    List<User> getAll();
    int getCount();
    
    void deleteAll();
    
    void update(User user);
}

```

**UserServiceImpl.java**
```java
package study.aop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import study.aop.domain.Level;
import study.aop.dao.UserDao;
import study.aop.domain.User;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PlatformTransactionManager transactionManager;
    private final MailSender mailSender;

    public static final int LOG_COUNT_FOR_SILVER=50;
    public static final int REC_COUNT_FOR_GOLD=30;

    public void upgradeLevels(){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            upgradeLevelsInternal();
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

    @Override
    public User get(String id) {
        return userDao.get(id).orElseThrow(()->{
            throw new RuntimeException();
        });
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public int getCount() {
        return userDao.getCount();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
    

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

```

**AppConfig.java**
```java
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final Environment env;

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource=new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcOperations jdbcOperations(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public UserDao userDao(){
        return new UserDaoImpl(jdbcOperations());
    }

    @Bean
    public MailSender mailSender(){
        return new DummyMailSender();
    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl(userDao(),mailSender());
    }
}
```

***
### 🚀 Transaction 코드 분리

**먼저, UserServiceImpl의 코드의 UpgradeLevels 메소드를 살펴봅시다.**
```java
public void upgradeLevels(){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            upgradeLevelsInternal();
            transactionManager.commit(status);
        }catch(Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }
```

지금 이 메소드는 Transaction 경계설정 코드와 비즈니스 로직 코드가 서로 공존하는 것을 볼 수 있습니다.

그렇다고 하더라도 코드를 잘 살펴보시면 서로간의 연관관계가 거의 없으며, 뚜렷이 각자의 기능들을 하고 있다는 것을 알아볼 수 있습니다.
1. Transaction 경계설정 코드는 시작과 종료만을 담당.
2. 스프링이 제공하는 Transaction (서비스 추상화가 되어있음!)과 계층 분리가 잘되어진 `Repository` 와 `Service`를 사용하기 때문에 비즈니스 로직 코드는
직접 DB를 다루지 않을 뿐더러 Transaction 경계기능을 하는 코드와 정보를 주고 받지 않습니다.

**결론적으로, 이 두가지 기능의 코드는 서로 성격이 다르다는 것을 볼 수 있습니다!**

**그렇다면, 현재로서는 Refactoring을 통하여 서로를 분리시켜 놓는 것이 최고의 방법이 될 것입니다! 👍**

#### ⚙️ 기능 분리 - Refactoring
비즈니스 로직 코드와 Transaction 경계 설정 코드를 분리하기 위해 가장 먼저 생각해볼 방법은 다음 두 가지 입니다.
1. 메소드를 이용한 분리
2. DI를 이용한 분리 

다시 한번 생각해보겠습니다. 어떻게 하면 더욱 깔끔할까?!

`어차피 서로 주고받을 정보가 없다면, 트랜잭션 코드를 없앨 수 없으니 비즈니스 로직에 트랜잭션 코드가 아예 안보이는 것처럼 사용하여보자!`

이런 방식의 해결법을 생각해보면, 메소드를 이용한 분리는 체택될 수 없습니다. 왜냐하면, 또 개발한 메소드가 원래의 클래스에 온전히 남아있기 때문입니다.

**즉, DI를 이용한 분리를 생각해봐야 합니다!**

**📌 DI를 이용한 분리**

어떤 원리를 통해 DI를 이용한 분리가 가능한지 살펴봅시다.

일단은, `UserService` 클래스는 `UserService`인터페이스를 구현한 클래스라는 것을 생각해볼 수 있습니다. 

즉, `UserServiceImpl`은 어떠한 클라이언트와도 강력한, 직접적인
결합이 되어있지 않고 유연한 확장이 가능한 상태입니다.

하지만, 일반적인 DI를 쓰는 이유를 생각해 본다면, 일반적으로 런타임시 한가지 구현 클래스를 갈아가며 사용하기 위하여 사용한다고 보시면 됩니다.

**여기서, 일반적이라는 말은 결코 정해진 제약이 아니니까, 일반적인 DI가 아닌 한 번에 두 가지 구현클래스를 동시에 이용해본다면 어떨까라는 생각을 할 수 있습니다.**

**다음과 같은 구조를 생각해볼 수 있습니다.**
- `UserService`를 구현하는 또다른 구현 클래스`UserServiceTx`를 만든다.
  - 이 구현 클래스는 트랜잭션 경계 설정 기능만을 책임으로 한다
- `UserServiceTx`는 트랜잭션 경계 설정책임만을 맡고, 비즈니스 로직은 또 다른 구현 클래스인 `UserServiceImpl`에게 맡긴다.

결과적으로, 이런 구조를 통해 위임을 위한 호출 작업 이전 이후에 트랜잭션 경계를 설정해주게 된다면, 
클라이언트 입장에서는 트랜잭션 경계설정이 부여된 비즈니스 로직을 사용하는 것과 똑같이 됩니다.

이러한 방법을 이용해 다음과 같은 코드를 구성할 수 있습니다.

**UserServiceTx.java**
```java
@RequiredArgsConstructor
public class UserServiceTx implements UserService{

    private final UserService userService;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void add(User user) {
        userService.add(user);
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.upgradeLevels();
            transactionManager.commit(status);
        }catch(RuntimeException e){
            transactionManager.rollback(status);
            throw e;
        }
    }
}

```

**UserServiceImpl.java - UpgradeLevels메소드**
```java
    public void upgradeLevels(){
        List<User> users=userDao.getAll();
        for(User user:users){
            if(checkUpgrade(user)){
                upgradeLevel(user);
            }
        }
    }
```

**AppConfig.java - DI**
```java
@Bean
public UserService userServiceTx(){
        return new UserServiceTx(userService(),transactionManager());
}
    
@Bean
public UserService userService(){
        return new UserServiceImpl(userDao(),mailSender());
}
```

코드를 살펴 보시면, 먼저 트랜잭션을 담당하는 오브젝트가 사용됨으로 트랜잭션 경계 설정을 해준 다음 실제 비즈니스 로직은
`UserServiceImpl`에 위임된 것을 볼 수 있습니다.

최종적으로 다음 그림과 같은 의존 관계가 구성되었다고 볼 수 있습니다.

<img width="1017" alt="스크린샷 2022-01-14 오전 4 08 04" src="https://user-images.githubusercontent.com/56334761/149393742-e28f4891-00ae-4d9e-a718-1d1511fa02c3.png">

이제 클라이언트는 `UserServiceTx` 빈을 호출하면 트랜잭션 경계설정이 된 비즈니스 로직을 사용할 수 있게 됩니다.
