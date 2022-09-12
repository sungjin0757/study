package basic.chunkorientedtasklet.config;

import basic.chunkorientedtasklet.domain.JpaWriterTestUser;
import basic.chunkorientedtasklet.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ItemProcessorConfiguration {
    @Bean
    public ItemProcessor<User, JpaWriterTestUser> itemProcessorV1() {
        return user -> new JpaWriterTestUser(user.getUserName(), user.getAge());
    }

    @Bean
    public ItemProcessor<User, JpaWriterTestUser> itemProcessorV2() {
        return user -> {
            boolean isValid = user.getId() % 2 ==0;
            if (isValid) {
                log.info("Ignored!");
                return null;
            }

            return new JpaWriterTestUser(user.getUserName(), user.getAge());
        };
    }
}
