package org.myproject.parking.pricing;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StandardPricingPolicy implements PricingPolicy {
    @Override
    public float getPrice(int hours, float price) {
        return hours * price;
    }

    @Override
    public PolicyType getType() {
        return PolicyType.STANDARD;
    }
}
