package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserWithTeamDto {
    private Long userId;
    private String userName;
    private int age;
    private Long teamId;
    private String teamName;

    @QueryProjection
    public UserWithTeamDto(Long userId, String userName, int age,
                           Long teamId, String teamName) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
