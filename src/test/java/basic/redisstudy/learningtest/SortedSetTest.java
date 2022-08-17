package basic.redisstudy.learningtest;

import basic.redisstudy.learningtest.entity.UserTestEntity;
import basic.redisstudy.learningtest.util.RedisTemplateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.Set;

@SpringBootTest
public class SortedSetTest {
    @Autowired
    RedisTemplateUtil redisTemplateUtil;
    @Resource(name = "redisTemplate")
    ZSetOperations<String, Object> zSetOps;

    UserTestEntity user1;
    UserTestEntity user2;
    UserTestEntity user3;

    @BeforeEach
    void setUp() {
        redisTemplateUtil.deleteAllKeys();
        user1 = new UserTestEntity("1", "Sungjin", "Hong", 27);
        user2 = new UserTestEntity("2", "Sungji", "Hon", 7);
        user2 = new UserTestEntity("3", "Sung", "Hon", 3);
    }

    @Test
    @DisplayName("SortedSet Test")
    void sortedSet() {
        zSetOps.add(user1.getId(), user1, 1.0);
        zSetOps.add(user1.getId(), user2, 2.0);
        zSetOps.add(user1.getId(), user3, 3.0);

        Set<ZSetOperations.TypedTuple<Object>> res = zSetOps.rangeByScoreWithScores(user1.getId(), 0, -1);
        for (ZSetOperations.TypedTuple<Object> re : res) {
            System.out.println("score = " + re.getScore());
            System.out.println("value = " + (UserTestEntity) re.getValue());
        }

        zSetOps.incrementScore(user1.getId(), user1, 3.0);
        Double score = zSetOps.score(user1.getId(), user1);

        Long rank1 = zSetOps.rank(user1.getId(), user1);
        Long rank2 = zSetOps.reverseRank(user1.getId(), user1);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(score, 4.0);
            Assertions.assertEquals(zSetOps.size(user1.getId()), 3);
            Assertions.assertEquals(rank1, 2);
            Assertions.assertEquals(rank2, 0);
        });
    }
}
