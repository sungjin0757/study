package basic.orderapi.member.command.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory

class MemberRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): MemberRepositoryCustom {
}