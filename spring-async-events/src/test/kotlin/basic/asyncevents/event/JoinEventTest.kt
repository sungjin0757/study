package basic.asyncevents.event

import basic.asyncevents.member.command.application.MemberRequest
import basic.asyncevents.member.command.application.MemberService
import basic.asyncevents.member.command.domain.value.MemberJoinedEvent
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents

@SpringBootTest
@RecordApplicationEvents
class JoinEventTest {
    @Autowired
    lateinit var memberService: MemberService
    @Autowired
    lateinit var events: ApplicationEvents

    @Test
    @DisplayName("이벤트 비동기 테스트")
    fun async_test() {
        val memberRequest = MemberRequest("hong", "123@naver.com")

        memberService.join(memberRequest)

        val count = events.stream(MemberJoinedEvent::class.java).count()
        Assertions.assertThat(count).isEqualTo(1)
    }
}