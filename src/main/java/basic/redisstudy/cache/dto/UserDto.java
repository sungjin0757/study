package basic.redisstudy.cache.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private int age;

    public UserDto(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private UserDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static UserDto fromRequestUserDto(RequestUserDto requestUserDto) {
        return new UserDto(requestUserDto.getName(), requestUserDto.getAge());
    }

    public static UserDto fromRequestUserDtoWithId(RequestUserDto requestUserDto, Long userId) {
        return new UserDto(userId, requestUserDto.getName(), requestUserDto.getAge());
    }
}
