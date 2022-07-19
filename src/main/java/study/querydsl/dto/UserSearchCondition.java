package study.querydsl.dto;

import lombok.Data;

@Data
public class UserSearchCondition {
    private String userName;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}
