package basic.shop.catalog.command.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@AllArgsConstructor
public class ProductId implements Serializable {
    @Column(name = "product_id")
    private String id;

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof ProductId productId))
            return false;
        return Objects.equals(id, productId.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
