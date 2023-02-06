package basic.orderapi.member.command.application

class DuplicateMemberException (
    var reason: String
): RuntimeException() {
}