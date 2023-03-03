package basic.asyncevents.common.event

import org.springframework.context.ApplicationEventPublisher

class Events {
    companion object {
        private lateinit var publisher: ApplicationEventPublisher

        fun setPublisher(applicationEventPublisher: ApplicationEventPublisher) {
            publisher = applicationEventPublisher
        }

        fun raise(event: Any) {
            if(publisher !== null) {
                publisher.publishEvent(event)
            }
        }
    }
}