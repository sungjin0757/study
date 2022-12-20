package study.querydsl.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@SequenceGenerator(
        name = "TEAM_SEQ_GENERATOR",
sequenceName = "TEAM_SEQ",
initialValue = 1,
allocationSize = 100)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "teamName"})
public class Team extends AbstractDateTraceEntity{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEAM_SQE_GENERATOR")
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    public Team(String teamName){
        this.teamName = teamName;
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = 31 * result + teamName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Team))
            return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) && Objects.equals(teamName, team.teamName);
    }
}
