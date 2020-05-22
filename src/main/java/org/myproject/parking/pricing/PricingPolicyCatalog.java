package org.myproject.parking.pricing;

import org.myproject.parking.pricing.policy.StandardPricingPolicy;
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
    public PricingPolicyCatalog(List<PricingPolicy> pricingPoliciesFromSpringContext) {
        pricingPolicies = pricingPoliciesFromSpringContext.stream().collect(Collectors.toMap(PricingPolicy::getType,
                Function.identity()));
    }

    public void registerPolicy(PolicyType PolicyType, PricingPolicy pricingPolicy) {
        pricingPolicies.put(PolicyType, pricingPolicy);
    }

    public void removePolicyByType(PolicyType PolicyType) {
        pricingPolicies.remove(PolicyType);
    }

    public PricingPolicy getPolicyByPolicyType(PolicyType PolicyType) {
        return pricingPolicies.getOrDefault(PolicyType, new StandardPricingPolicy());
    }

    public PricingPolicy getPolicyByPolicyTypeAsString(String policyTypeStr) {
        PolicyType currentPolicyType = PolicyType.valueOf(policyTypeStr.toUpperCase());
        return pricingPolicies.get(currentPolicyType);
    }
}
