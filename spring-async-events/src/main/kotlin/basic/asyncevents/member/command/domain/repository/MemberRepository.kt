package basic.asyncevents.member.command.domain.repository

import basic.asyncevents.member.command.domain.entity.Member
import basic.asyncevents.member.command.domain.value.MemberId
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, MemberId> {
}