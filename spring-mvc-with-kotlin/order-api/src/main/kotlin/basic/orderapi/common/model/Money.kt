package basic.orderapi.common.model

class Money (
    var value: Int
) {
    fun multiply(multiplier: Int): Money {
        return Money(value * multiplier)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }


}