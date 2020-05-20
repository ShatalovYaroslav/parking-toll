package org.myproject.parking.pricing;

import lombok.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@EqualsAndHashCode
@NoArgsConstructor
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
