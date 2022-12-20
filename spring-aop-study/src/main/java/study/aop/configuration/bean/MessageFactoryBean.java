package study.aop.configuration.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

@RequiredArgsConstructor
@Getter
public class MessageFactoryBean implements FactoryBean<Message> {
    private final String text;

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
