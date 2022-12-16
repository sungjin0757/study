package basic.shop.order.command.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"address", "receiver", "message"})
public class ShippingInfo {
    private Address address;
    private Receiver receiver;
    private String message;

}
