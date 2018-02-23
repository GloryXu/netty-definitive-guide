package netty.http.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Customer {

    private long customerNumber;

    private String firstName;

    private String lastName;

    private List<String> middleNames;

}
