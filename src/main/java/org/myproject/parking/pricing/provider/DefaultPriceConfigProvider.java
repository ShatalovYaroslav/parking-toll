
package org.myproject.parking.pricing.provider;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.BadPolicyParametersException;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.pricing.PricingConfig;
import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


@Log4j2
@Component
public class DefaultPriceConfigProvider extends PricingConfigProvider {

    @Autowired
    private PricingPolicyCatalog pricingPolicyCatalog;

    @Override
    protected PricingConfig getPriceConfig(String policyType, Map<String, String> parametersMap){
        return  PricingConfig.builder().policyType(PolicyType.valueOf(policyType))
                    .pricingParameters(parametersMap).build();
    }

    protected Set<String> getRequiredParameters(String policyType){
        PricingPolicy policy = pricingPolicyCatalog.getPolicyByPolicyTypeAsString(policyType);
        if(policy == null){
            throw new BadPolicyParametersException("No implementation for policyType: " + policyType);
        }
        return policy.getRequiredParameters();
    }

    @Override
    protected void validate(String policyType, Map<String, String> parametersMap) {
        Set<String> requiredParameterNames = getRequiredParameters(policyType);
        requiredParameterNames = Collections.unmodifiableSet(requiredParameterNames);

        checkNameParameters(parametersMap, requiredParameterNames);
    }

    @Override
    public boolean isMyType(String policyType) {
        return policyType.equalsIgnoreCase(PolicyType.STANDARD.toString());
    }

}
