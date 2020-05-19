package org.myproject.parking.model;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StandardPricingPolicy implements PricingPolicy{
    @Override
    public int getPrice(int hours, int price) {
        return hours * price;
    }
}
