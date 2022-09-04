package basic.chunkorientedtasklet.service;

import basic.chunkorientedtasklet.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomLogUserItemWriter implements ItemWriter<User> {
    @Override
    public void write(List items) throws Exception {
        for (Object item : items) {
            User user = (User) item;
            log.info("User = {}", user);
        }
    }
}
