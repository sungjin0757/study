package basic.orderapi.member.command.application

import basic.orderapi.member.command.domain.repository.MemberRepository
import basic.orderapi.member.command.domain.value.MemberId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberListService (
    val memberRepository: MemberRepository
) {
    fun getMembers(): List<MemberDto> {
        var memberDtos = memberRepository.searchToMemberDto()
        validateMemberDtos(memberDtos)

        return memberDtos
    }

    private fun validateMemberDtos(memberDtos: List<MemberDto>) {
        if(memberDtos.isNullOrEmpty())
            throw NotFoundMemberException("Not Found Member")
    }

    fun getMemberById(memberId: MemberId): MemberDto {
        var memberDto = memberRepository.searchById(memberId) ?: throw NotFoundMemberException("Not Found Member")
        return memberDto
    }
}