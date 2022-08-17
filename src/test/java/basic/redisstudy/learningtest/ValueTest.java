package basic.redisstudy.learningtest;

import basic.redisstudy.learningtest.entity.UserTestEntity;
import basic.redisstudy.learningtest.util.RedisTemplateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

@SpringBootTest
public class ValueTest {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    @Resource(name = "redisTemplate")
    ValueOperations<String, Object> valueOps;

    UserTestEntity user1;

    @BeforeEach
    void setUp() {
        user1 = new UserTestEntity("1", "Sungjin", "Hong", 27);
        redisTemplateUtil.deleteAllKeys();
    }

    @Test
    @DisplayName("ValueOperation Test")
    void value() {
        valueOps.set(user1.getId(), user1);

        LinkedHashMap user1Data = (LinkedHashMap) valueOps.get(user1.getId());

        String firstName = (String)user1Data.get("firstName");
        String lastName = (String)user1Data.get("lastName");
        Integer age = (Integer)user1Data.get("age");

        Assertions.assertAll(() -> {
           Assertions.assertEquals(firstName, "Sungjin");
           Assertions.assertEquals(lastName, "Hong");
           Assertions.assertEquals(age, 27);
        });

    }

}
