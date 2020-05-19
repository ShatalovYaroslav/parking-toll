package org.myproject.parking.pricing;

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
    public float getPrice(int hours, float price) {
        return hours * price;
    }
}
