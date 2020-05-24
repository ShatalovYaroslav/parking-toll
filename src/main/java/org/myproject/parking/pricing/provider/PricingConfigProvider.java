
package org.myproject.parking.pricing.provider;


import org.myproject.parking.exception.BadPolicyParametersException;
import org.myproject.parking.model.persistence.PricingConfig;

import java.util.Map;
import java.util.Set;


public abstract class PricingConfigProvider {

    public PricingConfig validateAndGetPriceConfig(String policyType, Map<String, String> parametersMap) {
        validate(policyType, parametersMap);
        return getPriceConfig(policyType, parametersMap);
    }

    protected abstract PricingConfig getPriceConfig(String policyType, Map<String, String> parametersMap);

    protected abstract void validate(String policyType, Map<String, String> parametersMap);

    public abstract boolean isMyType(String policyType);

    protected void checkNameParameters(final Map<String, String> parametersMap,
            final Set<String> requiredParameterNames) {
        requiredParameterNames.forEach(paramName -> {
            if (!parametersMap.containsKey(paramName)) {
                throw new BadPolicyParametersException("The parameter: '" + paramName +
                                           "' should be present in parking price config definition");
            }
        });
    }
}
