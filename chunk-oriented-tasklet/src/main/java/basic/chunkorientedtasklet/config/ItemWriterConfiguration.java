package basic.chunkorientedtasklet.config;

import basic.chunkorientedtasklet.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemWriterConfiguration {
    private final DataSource dataSource;

    @Bean
    public ItemWriter<User> customLogUserItemWriter() {
        return items -> {
            for (User user : items) {
                log.info("User = {}", user);
            }
        };
    }

}
