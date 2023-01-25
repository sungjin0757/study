package basic.orderapi.order.infra

import basic.orderapi.member.command.domain.repository.MemberRepository
import basic.orderapi.member.command.domain.value.MemberId
import basic.orderapi.order.command.domain.service.OrdererService
import basic.orderapi.order.command.domain.value.Orderer
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class OrdererServiceImpl (
    val memberRepository: MemberRepository
): OrdererService {
    override fun createOrderer(memberId: MemberId): Orderer {
        var findMember = memberRepository.findById(memberId).orElseThrow { IllegalArgumentException("No Member!") }
        return Orderer(memberId, findMember.name)
    }
}