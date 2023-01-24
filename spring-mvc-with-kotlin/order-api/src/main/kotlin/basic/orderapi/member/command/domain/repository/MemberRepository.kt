package basic.orderapi.member.command.domain.repository

import basic.orderapi.member.command.domain.entity.Member
import basic.orderapi.member.command.domain.value.MemberId
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, MemberId>, MemberRepositoryCustom, QuerydslPredicateExecutor<Member> {
}