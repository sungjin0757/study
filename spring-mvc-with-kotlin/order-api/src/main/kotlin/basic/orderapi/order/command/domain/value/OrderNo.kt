package basic.orderapi.order.command.domain.value

import java.io.Serializable
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class OrderNo (
    @Column(name = "order_number")
    var number: String
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderNo

        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        return number.hashCode()
    }

    companion object {
        fun generate(): OrderNo {
            var randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
            var number = String.format("%tY%<tm%<td%<tH-%d", Date(), randomNo)
            return OrderNo(number)
        }
    }
}