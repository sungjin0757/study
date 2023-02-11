package basic.orderapi.member.command.application

import basic.orderapi.common.model.Address

class MemberRequest (
    var name: String,
    var address: Address
) {
}