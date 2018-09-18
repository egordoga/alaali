package ua.alaali_dip.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderForm {

    private String recipient;
    private String recipientPhone;
    private String city;
    private Integer number;
}
