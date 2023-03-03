package basic.asyncevents.common.config

import basic.asyncevents.common.event.Events
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfig(
    val applicationContext: ApplicationContext
) {
    @Bean
    fun eventsInitializer(): InitializingBean {
        return InitializingBean { Events.setPublisher(applicationContext) }
    }
}