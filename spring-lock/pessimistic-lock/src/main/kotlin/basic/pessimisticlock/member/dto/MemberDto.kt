package basic.pessimisticlock.member.dto

import java.time.LocalDateTime

data class MemberDto(
    var memberId: String = "",
    var userName: String = "",
    var birthDate: LocalDateTime = LocalDateTime.now()
)
