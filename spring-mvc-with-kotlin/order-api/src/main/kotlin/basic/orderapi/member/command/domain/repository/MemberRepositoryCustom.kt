package basic.orderapi.member.command.domain.repository

import basic.orderapi.member.command.application.MemberDto
import basic.orderapi.member.command.domain.entity.Member
import basic.orderapi.member.command.domain.value.MemberId

interface MemberRepositoryCustom {
    fun searchToMemberDto(): List<MemberDto>
    fun searchById(memberId: MemberId): MemberDto?
    fun searchByName(memberName: String): Member?
}