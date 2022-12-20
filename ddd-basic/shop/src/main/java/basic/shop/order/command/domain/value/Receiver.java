package basic.shop.order.command.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"name", "phoneNumber"})
public class Receiver {
    private String name;
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Receiver))
            return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(name, receiver.name) && Objects.equals(phoneNumber, receiver.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = name == null ? 0 : name.hashCode();
        result = result * 31 + (phoneNumber == null ? 0 : phoneNumber.hashCode());
        return result;
    }
}

