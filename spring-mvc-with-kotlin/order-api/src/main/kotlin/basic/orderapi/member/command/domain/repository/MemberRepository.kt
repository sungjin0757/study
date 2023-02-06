package basic.orderapi.member.command.domain.repository

import basic.orderapi.member.command.domain.entity.Member
import basic.orderapi.member.command.domain.value.MemberId
import basic.orderapi.order.command.domain.value.OrderNo
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import java.util.*
import java.util.concurrent.ThreadLocalRandom

interface MemberRepository: CrudRepository<Member, MemberId>, MemberRepositoryCustom, QuerydslPredicateExecutor<Member> {
}