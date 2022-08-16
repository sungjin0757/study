package basic.redisstudy.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "redis")
@Getter
@AllArgsConstructor
public class RedisProperty {
    private String host;
    private int port;
}
