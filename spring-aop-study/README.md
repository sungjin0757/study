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

***

### 🚀 스프링 AOP

지금까지 해왔던 발전 기술을 다시 한번 살펴 봅시다.

1. `Service` 로직에서 `Transaction`부가기능의 분리를 위해 `DynamicProxy`와 `FactoryBean`을 도입하였습니다.
   - 문제점
     1. 한 번에 여러개의 클래스에 공통적인 부가기능을 부여하는 것은 불가능합니다. (`Factory Bean`의 설정의 중복을 막을 수 없다는 것을 뜻합니다.)
     2. 하나의 타깃에 여러가지 부가기능을 부여할수록 설정 파일이 복잡해집니다.
         - 예를 들어, Transaction 기능 외에 접근 제한 기능까지 추가하고 싶고 이 기능들을 공통적으로 사용하는 타깃이 수 백개라면 그 갯수만큼 설정 파일에서
           추가로 설정해 줘야 되기 때문입니다.
     3. `TransactionHandler` 오브젝트는 `FactoryBean`의 개수만큼 만들어 집니다. 위의 코드에서 보셨다 시피 타겟이 달라질 때마다,
       공통 기능임에도 불가하고 새로 `TransactionHandler`를 만들어 줘야 했습니다.
2. 문제점을 해결하기 위해 `SpringProxyFactoryBean`을 사용했습니다.
   - Advice의 도입
   - Pointcut의 도입
   - Advisor의 도입

이와 같은 과정으로 투명한 부가기능을 적용할 수 있었고, 타겟에는 비즈니스 로직만 유지한 채로 둘 수 있었습니다.
또한, 부가기능은 한 번만 만들어 모든 타겟과 메소드에서 재사용이 가능할 수 있도록 해놨습니다.

**But,** 한가지 문제점이 또 남았습니다.

그것은 바로 부가기능의 적용이 필요한 타겟 오브젝트마다 거의 비슷한 내용의 `ProxyFactoryBean` 빈 설정정보를 추가해주는
부분입니다.

```java
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class AppConfig {
    private final Environment env;
    
    //이 부분이 계속해서 늘어나게 됩니다.
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

위와 같은 코드가 계속해서 늘어나게 됩니다. 물론, 단순하고 쉬운 과정이지만 만약 저러한 오브젝트가 수 백개가 넘고 이렇게 되면
굉장히 번거로운 작업일 뿐더러 실수하기도 쉽게 됩니다.

**즉, 한 번에 여러 개의 빈에 프록시를 적용해야합니다!**

#### ⚙️ 빈 후처리기

먼저, 빈 후처리기란 이름 그대로 스프링 빈 오브젝트로 만들어지고 난 후에, 빈 오브젝트를 다시 가공할 수 있게 해주는 것입니다.

여기서 살펴볼 것은 빈이 생성된 이후에 Advisor를 이용한 자동 프록시 생성기인 `DefaultAdvisorAutoProxyCreator`를 살펴볼 수 있습니다.

`DefaultAdvisorAutoProxyCreator`가 빈 후처리기로 등록되어 있으면 스프링은 빈 오브젝트를 만들 때 마다 후처리기에게 빈을 보냅니다.
그 이후, 빈 후처리기는 빈으로 등록된 모든 Advisor내의 포인트컷을 이용해 전달받은 빈이 프록시 적용 대상인지 확인합니다.

프록시 적용 대상이라면, 내장된 프록시 생성기에게 현재 빈에 대한 프록시를 만들게 하고, 만들어진 프록시에 Advisor를  연결해줍니다.

이제, 프록시가 생성되면 원래 컨테이너가 전달해준 빈 오브젝트 대신 프록시 오브젝트를 컨테이너에게 돌려주게 됩니다.

결론적으로 컨테이너는 프록시 오브젝트를 빈으로 등록하고 사용하게 됩니다.

위의 설명을 토대로 부가기능을 부여할 빈을 선장하는 Pointcut이 연결된 Advisor를 등록하고, 빈 후처리기를 사용하게 된다면
복잡한 설정정보를 적을 필요없이 자동으로 프록시를 생성할 수 있게 됩니다.

**Pointcut의 확장**

스프링 `ProxyFactoryBean`을 할 때 Pointcut에서는 메소드를 어떻게 판정할지만 생각했습니다. 근데 빈 후처리기에서 설명한 바로는
Pointcut으로 어떤 빈이 선정 대상이 될 것인지 구별해야한다고 하고 있습니다.

어떻게 된 것일 까요??

Pointcut의 기능으로는 원래 메소드 선정 기능만 있는 것이 아닌 Class Filter또한 메소드로 갖고 있습니다.

즉, Pointcut은 프록시를 적용할 클래스인지 판단을 하고나서, 적용대상 클래스의 경우에는 Advice를 적용할 메소드인지 확인하는 방법으로 동작합니다.
결국은 이 두조건 모두를 만족하는 타겟에게만 부가기능이 부여되는 것입니다.

앞서 알아보았던 빈 후처리기인 `DefaultAdvisorAutoCreator`에서는 클래스와 메소드 선정이 모두 가능한 Pointcut이 필요합니다.

**NameMatchMethodPointcut**
위에서 등록했었던 Pointcut을 살펴봅시다.

```java
    @Bean
    public NameMatchMethodPointcut transactionPointcut(){
        NameMatchMethodPointcut pointcut=new NameMatchMethodPointcut();
        pointcut.setMappedNames("upgrade*");
        return pointcut;
    }
```

위와 같은 방식으로 등록을 했었습니다. 여기서 스프링이 기본 제공하는 `NameMethodPointcut`은
메소드 선정 기능만을 갖고있을 뿐, 클래스 필터의 기능은 존재하지 않습니다.

따라서, 이 클래스를 확장하여 클래스 필터의 기능으로서도 작동하도록 만들어 봅시다.

**NameMathClassMethodPointcut.java**
```java
public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

    public void setMappedClassName(String mappedClassName){
        this.setClassFilter(new SimpleFilter(mappedClassName));
    }

    @RequiredArgsConstructor
    static class SimpleFilter implements ClassFilter{
        private final String mappedName;

        @Override
        public boolean matches(Class<?> clazz) {
            return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
        }
    }
}

```

```java
public void setMappedClassName(String mappedClassName){
        this.setClassFilter(new SimpleFilter(mappedClassName));
    }
```
여기서 `setClassFilter`가 어떻게 나왔는지 의아하실수도 있습니다.

왜냐면, `NameMatchMethodPointcut`또한 `Pointcut`이라는 인터페이스를 implements했기 때문입니다.

```java
public interface Pointcut {
    Pointcut TRUE = TruePointcut.INSTANCE;

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
```
```java
public abstract class StaticMethodMatcherPointcut extends StaticMethodMatcher implements Pointcut {
    private ClassFilter classFilter;

    public StaticMethodMatcherPointcut() {
        this.classFilter = ClassFilter.TRUE;
    }

    public void setClassFilter(ClassFilter classFilter) {
        this.classFilter = classFilter;
    }

    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    public final MethodMatcher getMethodMatcher() {
        return this;
    }
}
```

```java
public class NameMatchMethodPointcut extends StaticMethodMatcherPointcut implements Serializable {
    private List<String> mappedNames = new ArrayList();

    public NameMatchMethodPointcut() {
    }

    public void setMappedName(String mappedName) {
        this.setMappedNames(mappedName);
    }

    public void setMappedNames(String... mappedNames) {
        this.mappedNames = new ArrayList(Arrays.asList(mappedNames));
    }

    public NameMatchMethodPointcut addMethodName(String name) {
        this.mappedNames.add(name);
        return this;
    }

    public boolean matches(Method method, Class<?> targetClass) {
        Iterator var3 = this.mappedNames.iterator();

        String mappedName;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            mappedName = (String)var3.next();
        } while(!mappedName.equals(method.getName()) && !this.isMatch(method.getName(), mappedName));

        return true;
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public boolean equals(@Nullable Object other) {
        return this == other || other instanceof NameMatchMethodPointcut && this.mappedNames.equals(((NameMatchMethodPointcut)other).mappedNames);
    }

    public int hashCode() {
        return this.mappedNames.hashCode();
    }

    public String toString() {
        return this.getClass().getName() + ": " + this.mappedNames;
    }
}
```
이런 식으로 구성되어 있습니다.  이로써, 클래스 필터기능 또한 가진 Pointcut을 작성 완료하였습니다.

이제, Advisor를 이용하는 자동 프록시 생성기가 어떤 순서로 작동을 하는지 알아보고 코드를 작성해봅시다.

1. `DefaultAdvisorAutoProxyCreator`는 등록된 빈 중에서 Advisor인터페이스를 구현한 것을 모두 찾습니다.
2. Advisor의 Pointcut을 적용해보면서 모든 빈에 대하여 프록시 적용 대상을 선정합니다.
3. 빈이 프록시 적용 대상이라면 프록시를 만들어 원래 빈 오브젝트와 바꿉니다.
4. 이제 원래 빈은 프록시를 통해 접근가능하도록 설정이 완료 됩니다.

👍 **참고로! `DefaultAdvisorAutoProxyCreator`는 빈으로만 등록해 두시면 됩니다.**

**AppConfig.java**
```java
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
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
    public TransactionAdvice transactionAdvice(){
        return new TransactionAdvice(transactionManager());
    }

    @Bean
    public NameMatchClassMethodPointcut transactionPointcut(){
        NameMatchClassMethodPointcut pointcut=new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
        pointcut.setMappedNames("upgrade*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(){
        return new DefaultPointcutAdvisor(transactionPointcut(),transactionAdvice());
    }
    
    //빈 후처리기 등록
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl(userDao(),mailSender());
    }


}
```

코드 작성또한 완료했으며, 빈 후처리기를 통한 부가기능 부여 또한 완벽히 수행하였습니다.

***

### 🚀 Pointcut 표현식

지금까지 발전해온 과정을 살펴보면, 일일이 클래스 필터와 메소드 매처를 구현하거나 기본적으로 스프링이 제공하는 기능을 사용해왔습니다.

지금까지는 단순히 클래스 이름이나 메소드 이름을 비교하는 것이 전부였다면, 일종의 표현식 언어를 사용하여 좀 더 세밀하게 선정 알고리즘을 짤 수 있습니다.

이렇게 고안된 것이 포인트컷 표현식이라고 합니다.

포인트컷 표현식은 `AspectJExpressionPointcut` 클래스를 사용하면 됩니다.

`NameMatchClassMethodPointcut`은 클래스와 메소드의 이름을 각각 독립적으로 비교한 반면에, 표현식으로는 한번에 지정가능하게 해줍니다.

`AspectJExpressionPointcut`의 이름을 보다시피, 스프링은 `AspectJ`라는 프레임워크에서 제공하는 것을 사용하게 되며, 이것을 AspectJ표현식이라 부릅니다.

#### 🔍 포인트컷 표현식 문법

AspectJ 포인트컷 표현식은 포인트컷 지시자를 이용해 작성합니다. 포인트컷 지시자중에서 가장 대표적으로 사용되는 것은 execution입니다.

`execution(접근제한자 타입패턴:return 타입 타입패턴:클래스 타입.이름패턴(메소드) (타입패턴:파라미터패턴) throws 예외패턴) `

1. : 는 설명을 의미합니다.
2. 접근제한자, 클래스 타입패턴, 예외패턴 등은 생략가능합니다.

**문법에 대한 자세한 설명은 생략하도록 하겠습니다!**

이제는 Pointcut표현식을 AspectJ 메소드의 파리미터로 하면 실행 가능하게 됩니다.

**Pointcut을 적용해 보도록 하겠습니다.**

포인트컷 표현식에는 위에서 잠시 언급했던 execution 외에도 bean을 선택하여주는 bean()메소드, 또한 특정 애노테이션이 타입, 메소드, 파라미터에 적용되어 있는 것을 보고
메소드를 선정하게 하는 포인트컷을 만들 수 있습니다. 예를 들면 `@Transactional` 과 같은 경우를 말합니다.

포인트컷 표현식은 AspectJExpressionPointcut빈을 등록하고 expression 프로퍼티에 넣어주면 됩니다. 클래스이름은 ServiceImpl로 끝나고 메소드 이름은
upgrade로 시작하는 모든 클래스에 적용되도록 코드를 짜봅시다.

**AppConfig.java**
```java
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
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
    public TransactionAdvice transactionAdvice(){
        return new TransactionAdvice(transactionManager());
    }

    //추가된 부분
    @Bean
    public AspectJExpressionPointcut transactionPointcut(){
        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(){
        return new DefaultPointcutAdvisor(transactionPointcut(),transactionAdvice());
    }
    
    //빈 후처리기 등록
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl(userDao(),mailSender());
    }


}
```

지금까지의 과정을 거쳐 `AspectJExpressionPointcut`의 적용까지 완료했습니다.

***

### 🚀 마지막으로, AOP란?! 

일반적인 객체지향 기술 방법으로는 독립적인 모듈화가 불가능하게 됩니다. 따라서, 부가기능 모듈화 작업은
기존의 객체지향 설계와는 다른의미가 있다는 뜻을 받아들여 `Aspect`라는 이름으로 부가기능 모듈화 작업을 뜻라게 되었습니다.

`Aspect`는 핵심 비즈니스로직 및 기능을 가지고 있지는 않지만 지금까지 해왔던 트랜잭션 경계 설정 부가기능을 추가한다던지 핵심기능에 부가되는 모듈을 지칭합니다.

이렇게 애플리케이션의 핵심적인 기능에서 부가기능을 분리해서 `Aspect`라는 모듈로 만들어서 개발하는 방법론을 `Aspect Oriented Progreamming` **AOP**라고 부릅니다.

한 가지 유의하실점은, AOP는 OOP에서 불리된 새로운 개념의 개발론이 아닌 OOP를 돕는 보조적인 기술이라고 보시면 될 것 같습니다.

즉, **AOP**는 `Aspect`를 분리함으로써 핵심 로직을 구현하는데 부담이 없도록 또한 최대한 객체지향 기술을 유지하도록 돕는 개발론이라고 할 수 있습니다.

#### 🔍 AOP 적용기술

**프록시를 이용한 AOP**

지금까지 저희가 해왔던 방식입니다. 프록시로 만들어서 DI로 연결된 빈 사이에 적용해 타겟의 메소드 호출 과정에 참여해서 부가기능을 제공하여 주는 것을 말합니다.

독립적으로 개발한 부가기능 모듈을 다양한 타겟 오브젝트의 메소드에 다이내믹하게 적용해주기 위해 가장 중요한 역할을 합니다. 따라서, 스프링 AOP는
프록시 방식의 AOP라고할 수 있습니다.

**바이트코드 생성과 조작을 통한 AOP**

AOP프레임워크의 대표격인 `AspectJ`는 프록시를 사용하지 않는 대표적인 AOP 기술입니다.

`AspectJ`는 프록시 처럼 간접적인 역할을 하는 것이 아니라 직접 타겟 오브젝트에 부가기능을 넣어주는 방법을 사용합니다. 그렇다고 하더라도 부가기능 코드를 직접 넣을수는 없으니, 
컴파일된 타겟의 클래스 파일 자체를 수정하거나 클래스가 JVM에 로딩되는 시점을 가로채서 바이트코드를 조작하는 복잡한 방법을 사용합니다.

이와 같이 번거로운 이유를 하는 이유는 
1. DI의 도움을 받지 않아도 됩니다.
   - 타겟 오브젝트를 직접 수정하는 방식이기 때문입니다.
2. 훨신, Detail하고 유연하게 AOP를 적용할 수 있습니다.
   - 바이트 코드를 직접 조작함으로서 AOP를 적용하면 오브젝트의 생성, 필드 값의 조회와 조작등 다양한 부가기능 부여가 가능합니다.

거의 일반적인 경우내에서는 프록시를 통해서 가능하지만, 좀 더 특별한 상황이 필요한 경우에 이와같은 방법을 쓴다고 합니다.

***
### 끝마치도록 하겠습니다. 👋