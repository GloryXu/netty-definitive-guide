package netty.http.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order {
    private long orderNumber;

    private Customer Customer;

    private Address billTo;

    private Shipping shipping;

    private Address shipTo;

    private Float total;
}
