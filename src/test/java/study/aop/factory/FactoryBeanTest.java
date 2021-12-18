package study.aop.factory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import study.aop.configuration.AppConfig;
import study.aop.configuration.bean.Message;
import study.aop.configuration.bean.MessageFactoryBean;

@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
public class FactoryBeanTest {
    @Autowired
    Message message;

    @Autowired
    MessageFactoryBean messageFactoryBean;

    @Test
    @DisplayName("Factory Bean Test")
    void 팩토리_빈_테스트(){
        Assertions.assertThat(message.getText()).isEqualTo("Factory");
        Assertions.assertThat(messageFactoryBean.getText()).isEqualTo("Factory");
    }
}
