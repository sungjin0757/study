package basic.redisstudy.cache.config;

import basic.redisstudy.cache.config.property.RedisCacheProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    private final RedisConnectionFactory redisConnection;
    private final ObjectMapper objectMapper;

    @Bean
    public CacheManager redisCacheManager() {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnection)
                .cacheDefaults(genRedisCacheDefaultConfiguration())
                .withInitialCacheConfigurations(genRedisCacheConfigurationCustomMap())
                .build();
    }

    private RedisCacheConfiguration genRedisCacheDefaultConfiguration() {
        return genRedisCacheConfiguration(RedisCacheProperty.DEFAULT_EXPIRE_SECONDS);
    }

    private Map<String, RedisCacheConfiguration> genRedisCacheConfigurationCustomMap(){
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(RedisCacheProperty.USER,
                genRedisCacheConfiguration(RedisCacheProperty.USER_EXPIRE_SECONDS));
        return cacheConfigurations;
    }

    private RedisCacheConfiguration genRedisCacheConfiguration(long ttl) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                )
                .entryTtl(Duration.ofSeconds(ttl));
        return redisCacheConfiguration;
    }

}
