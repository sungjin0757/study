package basic.orderapi.common.jpa

import basic.orderapi.common.model.Money
import javax.persistence.AttributeConverter

class MoneyConverter: AttributeConverter<Money, Int> {
    override fun convertToDatabaseColumn(attribute: Money?): Int? {
        return attribute?.value ?: null
    }

    override fun convertToEntityAttribute(dbData: Int?): Money? {
        return if(dbData !== null) Money(dbData) else null
    }
}