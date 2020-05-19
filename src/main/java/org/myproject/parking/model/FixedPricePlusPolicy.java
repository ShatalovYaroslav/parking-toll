package org.myproject.parking.model;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FixedPricePlusPolicy implements PricingPolicy{
    protected int fixedPrice;

    @Override
    public int getPrice(int hours, int price) {
        return hours * price;
    }
}
