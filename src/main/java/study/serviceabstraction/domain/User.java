package study.serviceabstraction.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.serviceabstraction.dao.Level;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {

    private String id;
    private String name;
    private String password;

    private Level level;
    private int login;
    private int recommend;

    public void updateLevel(Level level){
        this.level=level;
    }

    public void updateLogin(int login){
        this.login=login;
    }

    public void updateRecommend(int recommend){
        this.recommend=recommend;
    }

    @Builder(builderMethodName = "createUser")
    public User(String id, String name, String password,Level level,int login,int recommend){
        this.id=id;
        this.name=name;
        this.password=password;
        this.level=level;
        this.login=login;
        this.recommend=recommend;
    }
}
