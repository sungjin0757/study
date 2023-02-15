package basic.pessimisticlock.member.service

import basic.pessimisticlock.member.domain.value.MemberId
import basic.pessimisticlock.member.dto.MemberDto

interface MemberService {
    fun updateMember(memberDto: MemberDto): MemberId
}