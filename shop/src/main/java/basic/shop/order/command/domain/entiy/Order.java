package basic.shop.order.command.domain.entiy;

import basic.shop.common.model.AbstractBaseEntity;
import basic.shop.common.model.Money;
import basic.shop.order.command.domain.value.OrderNo;
import basic.shop.order.command.domain.value.OrderState;
import basic.shop.order.command.domain.value.ShippingInfo;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Access(AccessType.FIELD)
@Getter
@ToString(of = {"id", "orderLines", "totalAmounts", "shippingInfo", "orderState"})
public class Order extends AbstractBaseEntity {
    @EmbeddedId
    private OrderNo id;
    private List<OrderLine> orderLines = new ArrayList<>();
    private Money totalAmounts;
    private ShippingInfo shippingInfo;
    private OrderState orderState;

    public Order(List<OrderLine> orderLines, ShippingInfo shippingInfo) {
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
    }

    public void changeShippingInfo(ShippingInfo shippingInfo) {
        verifyNotYetShipped();
        this.shippingInfo = shippingInfo;
    }

    public void cancel() {
        verifyNotYetShipped();
        this.orderState = OrderState.CANCELED;
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyAtLeastOneOrMoreOrderLines(orderLines);
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    private void verifyAtLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("no OrderLine");
        }
    }

    private void calculateTotalAmounts() {
        int sum = orderLines.stream().mapToInt(OrderLine::getAmounts).sum();
        this.totalAmounts = new Money(sum);
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        try {
            Objects.requireNonNull(shippingInfo, "no ShippingInfo");
        } catch(NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        this.shippingInfo = shippingInfo;
    }

    private void verifyNotYetShipped() {
        if(orderState != OrderState.PAYMENT_WAITING && orderState != OrderState.PREPARING) {
            throw new IllegalArgumentException("already Shipped");
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Order order))
            return false;
        return Objects.equals(id, order.id) && isOrderLines(order.orderLines)
                && Objects.equals(totalAmounts, order.totalAmounts) && Objects.equals(shippingInfo, order.shippingInfo);
    }

    private boolean isOrderLines(List<OrderLine> orderLines) {
        int arrayCount = this.orderLines.size();
        int compareCount = (int) this.orderLines.stream().filter(orderLines::contains).count();
        return arrayCount == compareCount;
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = result * 31 + orderLines.hashCode();
        result = result * 31 + totalAmounts.hashCode();
        result = result * 31 + shippingInfo.hashCode();
        return result;
    }
}
