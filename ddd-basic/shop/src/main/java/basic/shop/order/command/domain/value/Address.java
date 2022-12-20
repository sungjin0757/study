package basic.shop.order.command.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"zipCode", "address1", "address2"})
public class Address {
    private String zipCode;
    private String address1;
    private String address2;

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Address))
            return false;
        Address address = (Address) o;
        return Objects.equals(zipCode, address.zipCode) && Objects.equals(address1, address.address1)
                && Objects.equals(address2, address.address2);
    }

    @Override
    public int hashCode() {
        int result = zipCode == null ? 0 : zipCode.hashCode();
        result = result * 31 + (address1 == null ? 0 : address1.hashCode());
        result = result * 31 + (address2 == null ? 0 : address2.hashCode());
        return result;
    }
}
