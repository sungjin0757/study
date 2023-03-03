package basic.asyncevents.member.command.domain.value

import basic.asyncevents.common.event.Event

class MemberJoinedEvent (
    var memberId: MemberId,
    var name: String,
    var email: String
): Event() {
}