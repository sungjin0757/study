package basic.shop.order.command.domain.value;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderNo implements Serializable {
    @Column(name = "order_number")
    private String number;

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof OrderNo))
            return false;
        OrderNo orderNo = (OrderNo) o;
        return Objects.equals(number, orderNo.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    public static OrderNo of(String number) {
        return new OrderNo(number);
    }
}
