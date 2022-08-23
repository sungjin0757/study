package basic.redisstudy.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisTemplateUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteAllKeys() {
        redisTemplate.keys("*").stream()
                .forEach(k -> redisTemplate.delete(k));
    }
}
