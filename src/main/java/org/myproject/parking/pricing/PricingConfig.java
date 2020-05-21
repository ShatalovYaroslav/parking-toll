package org.myproject.parking.pricing;

import lombok.*;

import java.util.Map;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PricingConfig {
    PolicyType policyType;
    Map<String, String> pricingParameters;
}
