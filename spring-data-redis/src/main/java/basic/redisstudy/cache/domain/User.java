package basic.redisstudy.cache.domain;

import basic.redisstudy.cache.dto.UserDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "age"})
public class User extends AbstractDateTraceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private int age;

    public void updateToDto(UserDto userDto) {
        this.name = userDto.getName();
        this.age = userDto.getAge();
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
