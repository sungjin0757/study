package study.exception.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private String id;
    private String name;
    private String password;

    @Builder(builderMethodName = "createUser")
    public User(String id,String name,String password){
        this.id=id;
        this.name=name;
        this.password=password;
    }
}
