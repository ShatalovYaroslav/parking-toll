package org.myproject.parking.pricing;

import java.util.Map;
import java.util.Set;

public interface PricingPolicy {
    float getPrice(int hours, float price, Map<String, String> pricingParameter);

    public PolicyType getType();

    public Set<String> getRequiredParameters();
}
