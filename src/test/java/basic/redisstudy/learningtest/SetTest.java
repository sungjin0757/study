package basic.redisstudy.learningtest;

import basic.redisstudy.learningtest.entity.UserTestEntity;
import basic.redisstudy.learningtest.util.RedisTemplateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
public class SetTest {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;
    @Resource(name = "redisTemplate")
    SetOperations<String, Object> setOps;

    UserTestEntity user1;
    UserTestEntity user2;

    @BeforeEach
    void setUp() {
        redisTemplateUtil.deleteAllKeys();
        user1 = new UserTestEntity("1", "Sungjin", "Hong", 27);
        user2 = new UserTestEntity("2", "Sungji", "Hon", 7);
    }

    @Test
    @DisplayName("SetOperation Test")
    void set() {
        setOps.add(user1.getId(), user1);
        setOps.add(user1.getId(), user1);

        setOps.add(user2.getId(), user1);
        setOps.add(user2.getId(), user2);

        String randomKey1 = UUID.randomUUID().toString();
        String randomKey2 = UUID.randomUUID().toString();
        String randomKey3 = UUID.randomUUID().toString();

        setOps.differenceAndStore(user2.getId(), user1.getId(), randomKey1);
        setOps.intersectAndStore(user2.getId(), user1.getId(), randomKey2);
        setOps.unionAndStore(user2.getId(), user1.getId(), randomKey3);


        Assertions.assertAll(() -> {
            Assertions.assertEquals(setOps.size(user1.getId()), 1);
            Assertions.assertEquals(setOps.size(user2.getId()), 2);
            Assertions.assertEquals(setOps.size(randomKey1), 1);
            Assertions.assertEquals(setOps.size(randomKey2), 1);
            Assertions.assertEquals(setOps.size(randomKey3), 2);

            Assertions.assertEquals(setOps.isMember(randomKey1, user2), true);
            Assertions.assertEquals(setOps.isMember(randomKey2, user1), true);
            Assertions.assertEquals(setOps.isMember(randomKey3, user1), true);
            Assertions.assertEquals(setOps.isMember(randomKey3, user2), true);
        });
    }
}
