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


***

### 🚀 Dynamic Proxy & Factory Bean

지금 까지 해왔던 과정의 특징을 살펴봅시다.

현재, `UserServiceTx`와 `UserServiceImpl` 상태로 분리되어 있습니다. 

부가 기능을 담고있는 클래스인 `UserServiceTx`는 부가기능외에 나머지 핵심 로직은 모두 `UserServiceImpl`에 위임하는 구조가 됩니다.

따라서 `UserServiceImpl`은 `UserServiceTx`의 존재 자체를 모르며, 부가기능이 핵심 기능을 사용하는 구조가 됩니다.

>**문제점**
> 
>클라이언트가 `UserServiceImpl`를 직접 사용해버리면?? 부가기능이 적용되지를 못합니다.

따라서, 이러한 사태를 방지하기 위하여 부가기능을 담은 클래스인 `UserServiceTx`를 마치 핵심기능을 가진 클래스처럼 꾸며 클라이언트가
이 클래스만을 사용하도록 만들어야 합니다.

그러기 위해서는 클라이언트는 직접 클래스를 사용하는 것이 아닌 인터페이스를 통해서만 기능을 사용하도록 하여야 합니다.

즉, 사용자는 저희가 만들어놓은
<img width="1017" alt="스크린샷 2022-01-14 오전 4 08 04" src="https://user-images.githubusercontent.com/56334761/149393742-e28f4891-00ae-4d9e-a718-1d1511fa02c3.png">
이 구조를 모르더라도 이런 구조를 사용하게끔 유도하는 것입니다.

**풀어서 이야기하자면 클라이언트는 인터페이스만을 보고 사용하기 때문에, 그 인터페이스가 비즈니스 로직을 가진 코드인줄 알테지만 사실은 부가기능 코드를 통해 접근하게 되는 것입니다.**

봐왔던 것처럼 클라이언트가 사용하려고 하는 실제 대상인것 처럼 위장해서 클라이언트의 요청을 받아주는 것,
지금의 `UserServiceTx`가 하는 역할을 대리자라는 뜻으로 <span style="color:red; font-weight:bold;">Proxy</span>라고 부르게 됩니다.

이런 프록시를 통해 위임받아 핵심기능을 처리하는 실제 오브젝트를 <span style="color:red; font-weight:bold;">Target</span>이라고 부릅니다.

**Proxy의 특징 & 사용이유**
1. Target과 같은 인터페이스를 구현, 프록시는 Target을 제어할 위치에 와야합.
2. 클라이언트가 타깃의 접근 방법 제어.
3. 타깃에 부가기능 부여 가능.

#### 🔍 데코레이터 패턴

데코레이터 패턴이란 일반적으로 런타임시에 타깃에게 부가적인 기능을 부여하기 위해 프록시를 사용하는 패턴을 말합니다.

즉, 런타임 시점에 부가적인 기능을 부여하기 때문에 코드상에서는 어떤 방식으로 프록시와 타깃이 연결되어있는지 알 수 없습니다.

데코레이터 패턴에서는 같은 인터페이스를 구현한 타겟과 여러개의 프록시를 만들 수 있습니다. 이는 부가기능을 예를 들어 Transaction뿐만아니라
타겟에 여러가지의 기능을 한 번에 부여시킬 수 있다는 것을 뜻합니다.

프록시로서 동작하는 각 데코레이터는 다음 단계가 데코레이터 프록시인지 최종 타겟인지를 모르기 때문에
다음 위임 대상은 인터페이스로 선언하며 외부에서 런타임 시에 주입받을 수 있도록 합니다.

즉, 스프링의 DI는 데코레이터 패턴을 적용시키기에 아주 편리하다고 할 수 있습니다.

**예를 들어,** 지금 구성한 `UserService`에 트랜잭션 기능과 더불어 또 다른 기능을 추가한다고 하면,

```java
@Bean
public UserService userServiceAnother(){
    return new UserServiceAnother(userServiceTx());
        }
        
@Bean
public UserService userServiceTx(){
        return new UserServiceTx(userService(),transactionManager());
        }

@Bean
public UserService userService(){
        return new UserServiceImpl(userDao(),mailSender());
        }
```

이런 식으로 필요하면 언제든지 데코레이터를 추가시킬 수 있게 됩니다.

#### 🔍 프록시 패턴

데코레이터 패턴은 최종 타겟에 기능을 부여하려는 목적으로 중간에 프록시가 끼어들었다면, 일반적으로 프록시 패턴은
최종 타겟에 대한 접근 제어를 위해 만들어진 경우를 뜻합니다.

더 자세히, 프록시 패턴은 타깃의 기능을 추가하는 것이 아닌 클라이언트가 타깃에 접근하는 방법을 바꿔준다고 생각하시면 편합니다.
즉, 타겟의 오브젝트를 생성하기가 복잡하거나 당장 필요하지 않은 경우 바로 오브젝트를 생성하는 것이 아닌 프록시를 생성해 놓았다가,
클라이언트가 메소드를 요청한 시점에 프록시가 타깃 오브젝트를 만들고, 요청을 위임해주는 것입니다.

>마치 **JPA의 프록시 패턴과도 유사합니다.**
> 
> <a href="https://velog.io/@sungjin0757/JPA-%ED%94%84%EB%A1%9D%EC%8B%9C%EC%A6%89%EC%8B%9C%EB%A1%9C%EB%94%A9-VS-%EC%A7%80%EC%97%B0%EB%A1%9C%EB%94%A9">JPA 프록시 패턴 보러가기 (즉시로딩, 지연로딩)</a>

구조적으로 보자면, 프록시 패턴 또한 다음 대상을 인터페이스를 통해 위임 가능하기 때문에 데코레이터 패턴과
 유사하다고 볼 수 있습니다. 다만, 데코레이터 패턴은 다음 대상이 무엇인지를 몰라도 되었지만 프록시 패턴의 프록시는
코드에서 자신이 만들거나 접근할 타겟의 정보를 알아야하는 경우가 많습니다. 왜냐하면, 타겟의 오브젝트를 만들어야 하는 프록시일 경우
 타겟에 대한 직접적인 정보를 알아야하기 때문입니다.

인터페이스를 통해 다음 대상을 위임하므로 결국은 데코레이터 패턴과 프록시 패턴 두 가지 경우를 혼합하여도 사용할 수도 있습니다.

<img width="627" alt="스크린샷 2022-01-16 오후 10 24 32" src="https://user-images.githubusercontent.com/56334761/149661818-42ed498a-da6e-4c09-ad2f-276427b2e44c.png">

이런 느낌으로 말이죠..

#### 🔍 Dynamic Proxy

프록시가 어떤 이유로 만들어 졌는지 또한 프록시가 어떤 방식으로 만들어 지는 지를 지금까지 알아보았습니다.

그 이유는 
- **첫번째로는,** 프록시를 구성하고 난 다음 타깃에게 위임하는 코드를 작성하기 번거롭다는 점입니다.
  
    왜냐하면, 클라이언트는 결국에는 프록시 객체를 이용하여 타깃에게 접근이 가능할 터인데, 타깃의 메소드가 많아질수록 위임해줘야하는 코드의 양은 길어질 것이며,
    기능이 추가거나 수정될 때 또한 함께 고쳐줘야한다는 문제점이 있습니다.
- **두번째로는,** 부가기능 코드 작성이 중복될 경우가 많다는 점입니다. 왜냐하면, 모든 메소드마다 똑같이 적용시켜야 할 지도 모르기 때문입니다.

이런 문제점을 해결할 수 있는것이 바로 **Dynamic Proxy**입니다.

Dynamic Proxy를 구성하기 전에 먼저 **리플렉션**에 대해서 알아봅시다.

리플렉션 API를 활용해 메소드에 대한 정의를 담은 Method 인터페이스를 활용해 메소드를 호출하는 방법을 알아봅시다.

**ArrayList**의 **size**라는 메소드를 추출한 뒤 **invoke**를 통해 추출해낸 메소드를 실행시켜 봅시다.

```java
@Test
    @DisplayName("Reflect - Method Test")
    void 리플렉트_메소드_추출_테스트() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method sizeMethod= ArrayList.class.getMethod("size");

        List<Integer> testList=new ArrayList<>();
        testList.add(1);
        testList.add(2);
        testList.add(3);
        
        Assertions.assertThat(testList.size()).isEqualTo(3);
        Assertions.assertThat(testList.size()).isEqualTo(sizeMethod.invoke(testList));
        Assertions.assertThat(sizeMethod.invoke(testList)).isEqualTo(3);
    }
```

**테스트 결과**

<img width="50%" alt="스크린샷 2022-01-19 오후 7 17 59" src="https://user-images.githubusercontent.com/56334761/150111020-ae021db9-597d-4b2d-aa3e-abbd3326b006.png">

보시는 것과 같이 Reflect를 활용해서 메소드에 대한 정보를 추출해낼 수 있었고, 이를 이용하여 지정한 오브젝트에 대하여 메소드를 실행시킬 수 있다는 것을 
확인하였습니다.

Dynamic Proxy의 동작 방법부터 살펴봅시다.

<img width="1095" alt="스크린샷 2022-01-19 오후 7 36 48" src="https://user-images.githubusercontent.com/56334761/150113847-ff0dc65e-cdc3-46be-9276-84563a949d15.png">

**Dynamic Proxy**란? 먼저 프록시 팩토리에 의해 런타임 시 다이내믹하게 만들어지는 프록시 입니다. 프록시 팩토리에게 `Interface`의 정보만
 넘겨주면 프록시를 적용한 오브젝트를 자동으로 만들어주게 됩니다.

이 과정에서, 추가시키고자 하는 부가기능을 `Invocation Handler`에 넣어주기만 하면 됩니다.

**InvocationHandler.java**
```java
public interface InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable;
}

```

`InvocationHancler` 인터페이스입니다. `invoke`라는 메소드는 위에서 진행해보았던 리플렉션 API의 Method 인터페이스와 타깃 메소드의 파라미터를 파라미터로 전달 받습니다.

즉, 클라리언트의 모든 요청 메소드는 `Dynamic Proxy`를 통하여 `InvocationHandler`의 `Invoke`메소드의 파라미터로 전달되며 
타깃 메소드에 부가기능을 적용시켜 그 결과를 리턴해줍니다.

이는 앞에서 봤던 두번째 문제점인 중복된 코드를 해결할 수 있습니다. `Invoke`라는 메소드 하나로 타깃 오브젝트의 메소드에 부가기능을 적용시켜 실행할 수 있기 때문입니다.

이제는, Transaction 부가기능을 Dynamic Proxy를 통하여 코드로 작성해봅시다.

**TransactionHandler.java**
```java
@RequiredArgsConstructor
public class TransactionHandler implements InvocationHandler {

    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().startsWith(pattern))
            return invokeWithTransaction(method,args);
        return method.invoke(target,args);
    }

    private Object invokeWithTransaction(Method method,Object[] args) throws Throwable{
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            Object invoke = method.invoke(target, args);
            transactionManager.commit(status);
            return invoke;
        }catch(InvocationTargetException e){
            transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}

```

이 코드에서는
1. Target Object
2. Transaction Manager
3. Method Pattern (부가기능을 지정된 메소드에만 적용시키기 위해)

들을 DI 시켜주는 부분을 유의하면 됩니다.

**Dynamic Proxy - Client에서 직접 생성**
```java
TransactionHandler txHandler=new TranscationHandler();

UserService userService=(UserService)Proxy.newProxyInstance(
        getClass().getClassLoader(),new Class[]{UserService.class},txHndler
        );
```

이렇게 Dynamic Proxy를 직접 생성해줄 수도 있습니다.

지금부터는, `TransactionHandler`와 `Dynamic Proxy`를 스프링 DI를 통해 사용할 수 있도록 만들면 됩니다.

하지만, `Dynamic Proxy`는 런타임 시에 동적으로 만들어지기 때문에 일반적인 스프링 Bean으로 등록할 수 없다는 것입니다.

#### 🔍 Factory Bean
스프링은 생성자를 통해 오브젝트를 만드는 방법 외에도 다양한 방법이 있습니다. 그 중 하나가 이 Factory Bean입니다.

Factory Bean 은 스프링을 대신해서 오브젝트의 생성로직을 담당하도록 만들어진 특별한 빈을 말합니다.

```java
public interface FactoryBean<T> {
    String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

    @Nullable
    T getObject() throws Exception;

    @Nullable
    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
```
이 인터페이스를 구현하기만 하면 됩니다.
- getObject() 메소드 내부에서 Dynamic Proxy를 생성한 후 반환시켜줍니다.

결론적으로 이 인터페이스를 구현한 클래스를 스프링의 빈으로 등록해주면 되는 것입니다.

추가로, 스프링은 `FactoryBean`인터페이스를 구현한 클래스가 빈의 클래스로 지정되면, 팩토리 빈 클래스의 getObject()를 통하여 오브젝트를 가져오고,
 이를 빈 오브젝트로 사용합니다. 빈의 클래스로 등록된 팩토리빈은 빈 오브젝트를 생성하는 과정에서만 사용됩니다.

`FactoryBean` 인터페이스를 구현한 클래스를 스프링 빈으로 만들어두면 getObject() 라는 메소드가 생성해주는 오브젝트가 실제 빈의
오브젝트로 대체 된다고 보시면 될 것 같습니다.

**코드를 통해 살펴봅시다.**

**TransactionFactoryBean.java**
```java
@RequiredArgsConstructor
@Getter
public class TransactionFactoryBean implements FactoryBean<Object> {

    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;
    private final Class<?> interfaces;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{interfaces},new TransactionHandler(target,
                transactionManager,pattern));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaces;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
```

**AppConfig.java - 스프링 빈 등록**
```java
    @Bean
    public TransactionFactoryBean userService(){
        return new TransactionFactoryBean(userServiceImpl(),transactionManager()
        ,"upgradeLevels()",UserService.class);
    }
```

**지금 까지, `Dynamic Proxy`와 `Factory Bean`을 적용해 보았습니다. 장점과 단점 또한 알아봅시다.**

**장점**
- 재사용이 가능합니다.
  - `Factory Bean`은 다양한 클래스에 적용가능합니다. 또한 하나 이상의 빈을 등록해도 상관 없습니다.
- 인터페이스를 구현하는 프록시 클래스를 일일이 만들어야 한다는 번거로움을 해결해줍니다.
- 부가적인 기능이 여러 메소드에 반복적으로 나타나게 되는 것을 해결해줍니다.

**단점**
- 한 번에 여러개의 클래스에 공통적인 부가기능을 부여하는 것은 불가능합니다. (`Factory Bean`의 설정의 중복을 막을 수 없다는 것을 뜻합니다.)
- 하나의 타깃에 여러가지 부가기능을 부여할수록 설정 파일이 복잡해집니다.
  - 예를 들어, Transaction 기능 외에 접근 제한 기능까지 추가하고 싶고 이 기능들을 공통적으로 사용하는 타깃이 수 백개라면 그 갯수만큼 설정 파일에서 
  추가로 설정해 줘야 되기 때문입니다.
- `TransactionHandler` 오브젝트는 `FactoryBean`의 개수만큼 만들어 집니다. 위의 코드에서 보셨다 시피 타겟이 달라질 때마다,
공통 기능임에도 불가하고 새로 `TransactionHandler`를 만들어 줘야 했습니다.

다음부터는, 이 단점들을 해결해나가 봅시다!

***

### 🚀 Spring Proxy Factory Bean

스프링은 일관된 방법으로 프록시를 만들 수 있게 도와주는 추상 레이어를 제공합니다. 스프링은 프록시 오브젝트를 생성해주는 기술을
추상화한 프록시 팩토리 빈을 제공하여 줍니다.

스프링의 `ProxyFactoryBean`은 프록시를 생성해서 빈 오브젝트로 등록하게 해주는 팩토리 빈이며,
순수하게 프록시를 생성하는 작업만들 담당하게 됩니다.

부가기능과 같은 작업은 별도의 빈에 둘 수 있습니다.

`ProxyFactoryBean`은 `InvocationHandler`가 아닌 `MethodInterceptor`를 사용합니다.

둘의 가장 큰 차이 점은
`InvocationHandler`는 target의 정보를 직접 알고 있어야 Method를 Invoke할 수 있었던 반면에,
```java
@Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().startsWith(pattern))
            return invokeWithTransaction(method,args);
        return method.invoke(target,args);
    }
```

`MethodInterceptor`는 target오브젝트에 대한 정보도 `ProxyFactoryBean`에게 제공받기 때문에, 타깃에 대한 정보를 직접 몰라도 됩니다.
디 덕분에 `MethodInterceptor`는 타깃과 상관 없이 독립적으로 만들 수 있으며, 싱긃톤 빈으로도 등록이 가능합니다.

이와 같은 정보를 기반으로 코드를 작성해 봅시다

**TransactionAdvice.java**
```java
@RequiredArgsConstructor
public class TransactionAdvice implements MethodInterceptor {
    private final PlatformTransactionManager transactionManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            Object ret = invocation.proceed();
            transactionManager.commit(status);
            return ret;
        }catch(RuntimeException e){
            transactionManager.rollback(status);
            throw e;
        }
    }
}

```

**AppConfig.java**
```java
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class AppConfig {
    private final Environment env;

    //Advice 부분 설명
    @Bean
    public TransactionAdvice transactionAdvice(){
        return new TransactionAdvice(transactionManager());
    }
    
    //Pointcut 부분 설명
    //NameMatchMethodPointcut은 스프링 기본 제공
    @Bean
    public NameMatchMethodPointcut transactionPointcut(){
        NameMatchMethodPointcut pointcut=new NameMatchMethodPointcut();
        pointcut.setMappedNames("upgrade*");
        return pointcut;
    }

    //Advisor = Advice + Pointcut
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(){
        return new DefaultPointcutAdvisor(transactionPointcut(),transactionAdvice());
    }
    
    @Bean
    public ProxyFactoryBean userService() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(userServiceImpl());
        factoryBean.setInterceptorNames("transactionAdvisor");
        return factoryBean;
    }
    
    ...
}
```
위의 코드를 보시다 시피, 타깃에대한 정보를 직접적으로 알고 있지 않습니다. `MethodInvocation`이라는 파라미터로 타깃에 대한
정보와 메소드에 대한 정보가 함께 넘어온다고 생각하시면 됩니다.

#### 🔍 Advice 어드바이스
**Target**이 필요 없는 순수한 부가기능을 뜻합니다.

`MethodInvocation`은 메소드 정보와 타깃 오브젝트가 담겨있는 파라미터입니다.
`MethodInvocation`은 타깃 오브젝트의 메소드를 실행할 수 있는 기능이 있기 때문에 `MethodInterceptor`는 부가기능에만
집중을 할 수 있습니다.

`MethodInvocation`은 proceed() 메소드를 실행하면 타겟 오브젝트의 메소드를 내부적으로 실행해주는 기능이 있습니다.

즉, `MethodInvocation`을 구현한 클래스를 클래스간 공유 가능하게 사용가능하다는 것입니다.

그냥 JDK에서의 `ProxyFactoryBean`의 단점이었던 **`TransactionHandler` 오브젝트는 `FactoryBean`의 개수만큼 만들어 집니다. 위의 코드에서 보셨다 시피 타겟이 달라질 때마다,
공통 기능임에도 불가하고 새로 `TransactionHandler`를 만들어 줘야 했습니다.** 이 문제를 해결할 수 있게 되었습니다.

또한, `MethodInterceptor`를 구현한 `TransactionAdvice`의 이름에서 알 수 있듯이 

<span style="color:red; font-weight:bold;">타겟 오브젝트에 적용하는 부가기능을 담은 오브젝트를 스프링에서는 어드바이스(Advice)라고 부르게 됩니다.</span>

마지막으로 다른 점이 있습니다.
`TransactionFactoryBean`을 사용했을 때는 `Dynamic Proxy`를 만들기 위해서 인터페이스 타입을 제공받아야 했습니다.

```java
//TransactionFactoryBean
@RequiredArgsConstructor
@Getter
public class TransactionFactoryBean implements FactoryBean<Object> {

    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;
    private final Class<?> interfaces;   //이 부분

    ...
}

```

하지만, 우리가 구현한 `Advice`에서는 따로 인터페이스의 정보를 제공받지 않아도 되었습니다. 그 이유는,
인터페이스의 정보를 제공하지 않아도 `ProxyFactoryBean`에는 인터페이스를 자동 검출하는 기능을 사용하여 타겟 오브젝트가
구현하고 있는 인터페이스 정보를 알아내기 때문입니다.

이렇게 Advice에 대해서 알아보았습니다. Advice는 타겟 오브젝트에 순수한 부가기능을 담은 오브젝트라고 아시면 됩니다.

#### 🔍 Pointcut 포인트컷
**부가기능 적용대상 메소드 선정 방법**을 뜻합니다.

`InvocationHandler`를 구현한`TransactionHandler`에서는 String 값으로 Pattern을 주입 받아 부가기능이 적용될 대상 메소드를 선정 하였습니다.

그렇다면 `MethodInterceptor`에서도 똑같이 pattern을 주입받아 내부 로직으로 처리하면 될까요?? 아닙니다!!

`MethodInterceptor`는 여러 프록시에서 공유해서 사용할 수 있습니다. 이 말은 즉, 타겟에 대한 정보를 직접적으로 가지고 있지 않다는 뜻과 같습니다.
때문에, 싱글톤형태인 스프링 빈으로도 등록할 수 있었던 것 입니다. 

더 자세히 보자면, `InvocationHandler`방식의 문제점이었던 `InvocationHandler`를 구현한 클래스가 `FactoryBean`을 만들 때마다 새로운 오브젝트가 생성
된다는 것이었습니다. 그 이유는 타겟마다 메소드 선정 알고리즘이나 타겟 자체가 다를 수 있기 때문에 어떤 타겟이나, 클래스에 종속되지 않기 위해서 입니다.

이 문제를 기껏 훌륭히 해결해 놨는데 Pattern을 주입 받아 활용한다면 또다시 어떤 메소드나 클래스에만 종속될 수 밖에 없다는 것을
의미합니다.

이런 문제점을 해결하기 위해서, 스프링은 부가기능을 제공하는 오브젝트인 Advice와 메소드 선정 알고리즘 오브젝트인 Pointcut을 따로 나누었습니다.
Advice와 Pointcut은 모두 주입을 받아 사용하며, 두 가지 모두 여러 프록시에서 공유가 가능하도록 만들어지기 때문에 스프링 빈으로 등록할 수 있습니다.

이제, 프록시는 클라이언트로부터 요청을 받으면 먼저 Pointcut에게 적용 가능한 메소드인지 확인을 한 뒤, Advice를 호출해 주면 됩니다.

결과적으로, Advice와 Pointcut의 도입으로 인해 여러 프록시가 공유하며 유연하게 사용할 수 있게 되었고, 구체적인 부가기능 방칙이나 메소드 선정 알고르짐이 바뀌게 되면
Advice나 Pointcut만 바꿔주면 해결되게 되었습니다.

`OCP : Open Closed Priciple`을 잘 지켰다고 볼 수 있습니다. 
><a href="https://github.com/sungjin0757/spring-dependency-study"><span style="font-weight:bold;">OCP 더 자세히 보기</span></a>

#### 👍 추가로, Advisor란?
Advisor란 Advice와 Pointcut을 묶는다고 보시면 됩니다.

묶는 이유는, `ProxyFactoryBean`에 여러가지 Advice와 Pointcut이 추가 될 수 있습니다.

여기서, 각각의 Advice마다 메소드를 선정하는 방식이 달라질 수도 있으니 어떤 Pointcut을 적용할지 애매해질 수 있읍니다. 그렇기 때문에 Advice와 Pointcut을 하나로
묶어서 사용합니다.
