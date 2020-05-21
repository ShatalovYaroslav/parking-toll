package org.myproject.parking.pricing;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FixedPricePlusPolicy implements PricingPolicy {
    protected float fixedPrice;

    @Override
    public float getPrice(int hours, float price) {
        return fixedPrice + hours * price;
    }

    @Override
    public PolicyType getType() {
        return PolicyType.FIXED_PLUS;
    }
}
