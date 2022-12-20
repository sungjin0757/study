package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;
import study.querydsl.repository.UserJpaRepository;
import study.querydsl.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;

    @GetMapping("/v1/users")
    private ResponseEntity<List<UserWithTeamDto>> searchUserV1(UserSearchCondition condition){
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.search(condition));
    }

    @GetMapping("/v2/users")
    private ResponseEntity<Page<UserWithTeamDto>> searchUserV2(UserSearchCondition condition, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.searchPageSim(condition, pageable));
    }

    @GetMapping("/v3/users")
    private ResponseEntity<Page<UserWithTeamDto>> searchUserV3(UserSearchCondition condition, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.searchComp(condition, pageable));
    }
}
