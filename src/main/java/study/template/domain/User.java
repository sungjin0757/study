package study.template.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    String id;
    String name;
    String password;

    @Builder(builderMethodName = "createUser")
    public User(String id,String name,String password){
        this.id=id;
        this.name=name;
        this.password=password;
    }
}
