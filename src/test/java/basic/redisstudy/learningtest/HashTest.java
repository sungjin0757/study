package basic.redisstudy.learningtest;

import basic.redisstudy.learningtest.entity.UserTestEntity;
import basic.redisstudy.learningtest.util.RedisTemplateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
public class HashTest {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;
    @Resource(name = "redisTemplate")
    HashOperations<String, Object, Object> hashOps;

    UserTestEntity user1;

    @BeforeEach
    void setUp() {
        redisTemplateUtil.deleteAllKeys();
        user1 = new UserTestEntity("1", "Sungjin", "Hong", 27);
    }

    @Test
    @DisplayName("HashOperation Test")
    void hash() {
        hashOps.put(user1.getId(), "firstName", user1.getFirstName());
        hashOps.put(user1.getId(), "lastName", user1.getLastName());
        hashOps.put(user1.getId(), "age", user1.getAge());

        String firstName = (String) hashOps.get(user1.getId(), "firstName");
        String lastName = (String) hashOps.get(user1.getId(), "lastName");
        Integer age = (Integer) hashOps.get(user1.getId(), "age");

        Map<Object, Object> entries = hashOps.entries(user1.getId());
        Long size = hashOps.size(user1.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(firstName, "Sungjin");
            Assertions.assertEquals(lastName, "Hong");
            Assertions.assertEquals(age, 27);
            Assertions.assertEquals(size, 3l);
            org.assertj.core.api.Assertions.assertThat(entries.keySet()).contains("firstName", "lastName", "age");
            org.assertj.core.api.Assertions.assertThat(entries.values()).contains(firstName, lastName, age);
        });
    }
}
