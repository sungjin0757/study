package basic.shop.order.command.domain.value;

import basic.shop.catalog.command.domain.Product;
import basic.shop.catalog.command.domain.ProductId;
import basic.shop.common.model.AbstractBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@ToString(of = {"id", "price", "quantity", "amounts", "product"})
public class OrderLine extends AbstractBaseEntity {
    @EmbeddedId
    private ProductId id;
    private int price;
    private int quantity;
    private int amounts;
    private Product product;

    private int calculateAmounts() {
        return price * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof OrderLine))
            return false;
        OrderLine orderLine = (OrderLine) o;
        return Objects.equals(id, orderLine.id) && Objects.equals(price, orderLine.price)
                && Objects.equals(quantity, orderLine.quantity) && Objects.equals(amounts, orderLine.amounts)
                && Objects.equals(product, orderLine.product);
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = result * 31 + Integer.hashCode(price);
        result = result * 31 + Integer.hashCode(quantity);
        result = result * 31 + Integer.hashCode(quantity);
        result = result * 31 + Integer.hashCode(amounts);
        result = result * 31 + Objects.hashCode(product);
        return result;
    }
}
