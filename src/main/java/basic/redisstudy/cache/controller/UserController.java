package basic.redisstudy.cache.controller;

import basic.redisstudy.cache.domain.User;
import basic.redisstudy.cache.dto.RequestUserDto;
import basic.redisstudy.cache.dto.UserDto;
import basic.redisstudy.cache.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody RequestUserDto requestUserDto) {
        UserDto userDto = UserDto.fromRequestUserDto(requestUserDto);
        User saveUser = userService.save(userDto);

        return genResponseEntity(saveUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUser(@PathVariable Long userId) {
        User findUser = userService.findById(userId);

        return genResponseEntity(findUser, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody RequestUserDto requestUserDto) {
        UserDto userDto = UserDto.fromRequestUserDtoWithId(requestUserDto, userId);
        User updateUser = userService.update(userDto);

        return genResponseEntity(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);

        return genResponseEntity("Delete Completed!", HttpStatus.OK);
    }

    private <T> ResponseEntity<T> genResponseEntity(T object, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(object);
    }
}
