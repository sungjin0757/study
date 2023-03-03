package basic.asyncevents.member.command.application

import basic.asyncevents.member.command.domain.value.MemberJoinedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class MemberJoinedEventHandler {
    @Async
    @TransactionalEventListener(
        classes = [MemberJoinedEvent::class],
        phase = TransactionPhase.AFTER_COMMIT
    )
    fun handle(event: MemberJoinedEvent) {
        println("===== Event Occurred =====")
        println("memberId = ${event.memberId} \nname = ${event.name}\nemail = ${event.email}\ntime = ${event.timestamp}")
    }
}