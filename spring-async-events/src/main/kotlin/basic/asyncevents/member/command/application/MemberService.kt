package basic.asyncevents.member.command.application

import basic.asyncevents.member.command.domain.entity.Member
import basic.asyncevents.member.command.domain.repository.MemberRepository
import basic.asyncevents.member.command.domain.value.MemberId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService (
    val memberRepository: MemberRepository
) {
    @Transactional
    fun join(memberRequest: MemberRequest): MemberId {
        val memberId = MemberId.generate()
        val member = Member.of(memberId, memberRequest.name, memberRequest.email)

        memberRepository.save(member)
        return memberId
    }
}