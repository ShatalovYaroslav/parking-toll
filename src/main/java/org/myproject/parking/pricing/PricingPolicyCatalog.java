package org.myproject.parking.pricing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class PricingPolicyCatalog {

    private final Map<PolicyType, PricingPolicy> pricingPolicies;

    @Autowired
    public PricingPolicyCatalog(List<PricingPolicy> PricingPoliciesFromSpringContext) {
        pricingPolicies = PricingPoliciesFromSpringContext.stream().collect(Collectors.toMap(PricingPolicy::getType,
                Function.identity()));
    }

    public void registerPolicy(PolicyType PolicyType, PricingPolicy PricingPolicy) {
        pricingPolicies.put(PolicyType, PricingPolicy);
    }

    public void removePolicyByType(PolicyType PolicyType) {
        pricingPolicies.remove(PolicyType);
    }

    public PricingPolicy getPolicyByPolicyType(PolicyType PolicyType) {
        return pricingPolicies.getOrDefault(PolicyType, new StandardPricingPolicy());
    }

    public PricingPolicy getPolicyByPolicyTypeAsString(String PolicyTypeStr) {
        PolicyType currentPolicyType = PolicyType.valueOf(PolicyTypeStr.toUpperCase());
        return pricingPolicies.get(currentPolicyType);
    }
}
