package basic.chunkorientedtasklet.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@ToString(of = {"id", "userName", "age"})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username")
    private String userName;
    @Column(name = "age")
    private int age;

    public User(Long id, String userName, int age) {
        this.id = id;
        this.userName = userName;
        this.age = age;
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof User))
            return false;
        User u = (User) o;
        return Objects.equals(u.id, id) && Objects.equals(u.userName, userName)
                && Objects.equals(u.age, age);
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = result * 31 + userName.hashCode();
        result = result * 31 + Integer.hashCode(age);
        return result;
    }
}
