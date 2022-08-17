package basic.redisstudy.learningtest.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString(of= {"id", "firstName", "lastName", "age", "createdDate"})
public class UserTestEntity implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDateTime createdDate;

    public UserTestEntity(String id, String firstName, String lastName, Integer age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.createdDate = LocalDateTime.now();
    }
}
