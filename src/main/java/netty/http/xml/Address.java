package netty.http.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {

    private String street1;

    private String street2;

    private String city;

    private String state;

    private String postCode;

    private String country;
}
