package study.serviceabstraction.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.serviceabstraction.dao.Level;

import java.time.LocalDateTime;

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
