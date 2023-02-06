package basic.orderapi.member.command.domain.repository

import basic.orderapi.member.command.application.MemberDto
import basic.orderapi.member.command.domain.entity.Member
import basic.orderapi.member.command.domain.entity.QMember
import basic.orderapi.member.command.domain.entity.QMember.*
import basic.orderapi.member.command.domain.value.MemberId
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory


class MemberRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): MemberRepositoryCustom {
    override fun searchToMemberDto(): List<MemberDto> {
        return jpaQueryFactory
            .select(Projections.fields(MemberDto::class.java, member.id, member.name, member.address))
            .from(member)
            .fetch()
    }

    override fun searchById(memberId: MemberId): MemberDto? {
        return jpaQueryFactory
            .select(Projections.fields(MemberDto::class.java, member.id, member.name, member.address))
            .from(member)
            .where(member.id.eq(memberId))
            .fetchOne()
    }

    override fun searchByName(memberName: String): Member? {
        return jpaQueryFactory
            .selectFrom(member)
            .where(member.name.eq(memberName))
            .fetchOne()
    }
}