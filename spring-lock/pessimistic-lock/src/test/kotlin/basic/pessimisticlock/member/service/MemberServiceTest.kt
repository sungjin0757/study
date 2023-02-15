package basic.pessimisticlock.member.service

import basic.pessimisticlock.member.domain.entity.Member
import basic.pessimisticlock.member.domain.repository.MemberRepository
import basic.pessimisticlock.member.domain.value.Age
import basic.pessimisticlock.member.domain.value.MemberId
import basic.pessimisticlock.member.dto.MemberDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.collections.HashSet

@SpringBootTest
class MemberServiceTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Nested
    @DisplayName("비관적 락 테스트")
    inner class PessimisticLockTest {
        @Autowired
        lateinit var memberService: TestMemberService

        private var memberId: MemberId = MemberId.generate()
        private var birthDate: LocalDateTime = LocalDateTime.now()

        @BeforeEach
        fun init() {
            val member = Member(memberId, "Hong", Age.generate(birthDate))
            memberRepository.save(member)

        }

        @Test
        @DisplayName("멀티 스레드 테스트 - 락이 있을 때")
        fun lockingTest() {
            val numOfExecute = 1000
            val service = Executors.newFixedThreadPool(numOfExecute)
            val latch = CountDownLatch(numOfExecute)

            for (i in 0 until numOfExecute) {
                service.execute() {
                    try {
                        val memberDto = MemberDto(memberId.id!!, "Hong-${UUID.randomUUID()}", LocalDateTime.now())
                        memberService.updateMember(memberDto!!)
                        println("성공")
                    } catch (oe: PessimisticLockingFailureException) {
                        println("충돌 감지")
                    } catch (e: Exception) {
                        println(e.message)
                    }
                    latch.countDown()
                }
            }
            latch.await()

            Assertions.assertThat(memberService.valueWithLockingSet.size).isEqualTo(numOfExecute)
        }

        @Test
        @DisplayName("멀티 스레드 테스트 - 락이 없을 때")
        fun noLockingTest() {
            val numOfExecute = 1000
            val service = Executors.newFixedThreadPool(numOfExecute)
            val latch = CountDownLatch(numOfExecute)

            for (i in 0 until numOfExecute) {
                service.execute() {
                    try {
                        val memberDto = MemberDto(memberId.id!!, "Hong-${UUID.randomUUID()}", LocalDateTime.now())
                        memberService.updateMemberWithoutLocking(memberDto!!)
                        println("성공")
                    } catch (oe: PessimisticLockingFailureException) {
                        println("충돌 감지")
                    } catch (e: Exception) {
                        println(e.message)
                    }
                    latch.countDown()
                }
            }
            latch.await()

            Assertions.assertThat(memberService.valueWithoutLockingSet.size).isLessThanOrEqualTo(numOfExecute)
        }
    }

    companion object {
        @Component
        class TestMemberService (
            val memberRepository: MemberRepository
        ): MemberService {
            var valueWithLockingSet = HashSet<String>()
            var valueWithoutLockingSet = HashSet<String>()

            @Transactional
            override fun updateMember(memberDto: MemberDto): MemberId {
                val memberId = MemberId.of(memberDto.memberId)

                val findMember = memberRepository.findByIdForUpdate(memberId)
                    .orElseThrow { throw IllegalArgumentException("Not Found Member!") }

                valueWithLockingSet.add(memberDto.userName)

                findMember.age = Age.generate(memberDto.birthDate)
                findMember.userName = memberDto.userName
                return findMember.id!!
            }

            @Transactional
            fun updateMemberWithoutLocking(memberDto: MemberDto): MemberId {
                val memberId = MemberId.of(memberDto.memberId)

                val findMember = memberRepository.findById(memberId)
                    .orElseThrow { throw IllegalArgumentException("Not Found Member!") }

                valueWithoutLockingSet.add(memberDto.userName)

                findMember.age = Age.generate(memberDto.birthDate)
                findMember.userName = memberDto.userName
                return findMember.id!!
            }
        }
    }
}