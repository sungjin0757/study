package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;

import java.util.List;

public interface UserRepositoryCustom{
    List<UserWithTeamDto> search(UserSearchCondition condition);
    Page<UserWithTeamDto> searchPageSim(UserSearchCondition condition, Pageable pageable);
    Page<UserWithTeamDto> searchComp(UserSearchCondition condition, Pageable pageable);
}
