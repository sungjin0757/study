package basic.orderapi.member.command.application

class NotFoundMemberException (
    var reason: String
): RuntimeException(reason) {

}