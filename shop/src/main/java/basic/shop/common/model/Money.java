package basic.shop.common.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = {"value"})
@AllArgsConstructor
public class Money {
    private int value;

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Money))
            return false;
        Money money = (Money) o;
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
