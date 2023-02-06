package basic.orderapi.member.command.application

import basic.orderapi.common.model.Address
import basic.orderapi.member.command.domain.value.MemberId

class MemberDto(
    var memberId: MemberId,
    var name: String,
    var address: Address
) {
}