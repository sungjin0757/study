package basic.orderapi.member.command.application

import basic.orderapi.member.command.domain.entity.Member
import basic.orderapi.member.command.domain.repository.MemberRepository
import basic.orderapi.member.command.domain.value.MemberId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignUpMemberService(
    val memberRepository: MemberRepository
) {
    @Transactional
    fun signUp(memberRequest: MemberRequest): MemberId {
        validateDuplicateMember(memberRequest.name)

        var memberId = MemberId.generate()

        var saveMember = Member(memberId, memberRequest.name, memberRequest.address)
        memberRepository.save(saveMember)
        return memberId
    }

    private fun validateDuplicateMember(memberName: String) {
        if (memberRepository.searchByName(memberName) !== null)
            throw DuplicateMemberException("$memberName is Already Exists")
    }
}