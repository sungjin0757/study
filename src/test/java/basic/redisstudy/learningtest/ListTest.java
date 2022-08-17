package basic.redisstudy.learningtest;

import basic.redisstudy.learningtest.entity.UserTestEntity;
import basic.redisstudy.learningtest.util.RedisTemplateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest
public class ListTest {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;
    @Resource(name = "redisTemplate")
    ListOperations<String, Object> listOps;

    UserTestEntity user1;
    UserTestEntity user2;
    UserTestEntity user3;

    @BeforeEach
    void setUp() {
        redisTemplateUtil.deleteAllKeys();
        user1 = new UserTestEntity("1", "Sungjin", "Hong", 27);
        user2 = new UserTestEntity("2", "Sugjin", "Hon", 7);
        user3 = new UserTestEntity("3", "ngjin", "ong", 2);
    }

    @Test
    @DisplayName("ListOperation Test")
    void list() {
        listOps.leftPush("key1", user1);
        listOps.leftPush("key1", user2);
        listOps.rightPush("key1", user3);

        List<Object> key1 = listOps.range("key1", 0, -1);
        listOps.rightPop("key1");
        List<Object> key2 = listOps.range("key1", 0, -1);

        Assertions.assertAll(() ->{
            Assertions.assertEquals(key1.size(), 3);
            Assertions.assertEquals(key2.size(), 2);
            LinkedHashMap map1 = (LinkedHashMap) key1.get(key1.size() - 1);
            LinkedHashMap map2 = (LinkedHashMap) key2.get(key2.size() - 1);

            Assertions.assertEquals(map1.get("firstName"), "ngjin");
            Assertions.assertEquals(map2.get("firstName"), "Sungjin");
        });
    }


}
