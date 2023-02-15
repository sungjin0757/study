package basic.pessimisticlock.member.domain.repository

import basic.pessimisticlock.member.domain.entity.Member
import basic.pessimisticlock.member.domain.value.MemberId
import javax.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param
import java.util.Optional
import javax.persistence.QueryHint

interface MemberRepository: JpaRepository<Member, MemberId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "2000"))
    @Query("select m from Member m where m.id = :id")
    fun findByIdForUpdate(@Param("id") id: MemberId): Optional<Member>
}