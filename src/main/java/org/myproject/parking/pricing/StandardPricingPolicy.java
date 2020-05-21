package org.myproject.parking.pricing;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Component
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StandardPricingPolicy implements PricingPolicy {
    @Override
    public float getPrice(int hours, float price,  Map<String, String> pricingParameter) {
        return hours * price;
    }

    @Override
    public PolicyType getType() {
        return PolicyType.STANDARD;
    }

    @Override
    public Set<String> getRequiredParameters(){
        return new HashSet<>();
    }
}
