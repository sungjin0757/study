package basic.pessimisticlock.member.service

import basic.pessimisticlock.member.domain.repository.MemberRepository
import basic.pessimisticlock.member.domain.value.Age
import basic.pessimisticlock.member.domain.value.MemberId
import basic.pessimisticlock.member.dto.MemberDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl (
    val memberRepository: MemberRepository
): MemberService {
    @Transactional
    override fun updateMember(memberDto: MemberDto): MemberId {
        val memberId = MemberId.of(memberDto.memberId)

        val findMember = memberRepository.findByIdForUpdate(memberId)
            .orElseThrow { throw IllegalArgumentException("Not Found Member!") }

        findMember.age = Age.generate(memberDto.birthDate)
        findMember.userName = memberDto.userName
        return findMember.id!!
    }
}