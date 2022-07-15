package study.querydsl.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@SequenceGenerator(name = "USER_SEQ_GENERATOR",
    sequenceName = "USER_SEQ",
    initialValue = 1, allocationSize = 100)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "age"})
public class User extends AbstractDateTraceEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "USER_SEQ_GENERATOR")
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public User(String userName){
        this(userName, 0 ,null);
    }

    public User(String userName, int age){
        this(userName, age, null);
    }

    public User(String userName, int age, Team team){
        this.userName = userName;
        this.age = age;
        if(team != null)
            changeTeam(team);
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getUsers().add(this);
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = result * 31 + userName.hashCode();
        result = result * 31 + Integer.hashCode(age);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName)
                && Objects.equals(age, user.age);
    }
}
