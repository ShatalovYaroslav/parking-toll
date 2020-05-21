
package org.myproject.parking.pricing.provider;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.BadPolicyParametersException;
import org.myproject.parking.pricing.PolicyType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.myproject.parking.pricing.FixedPricePlusPolicy.FIXED_PRICE_PARAMETER;


@Log4j2
@Component
public class FixedPlusPriceConfigProvider extends DefaultPriceConfigProvider {

    @Override
    protected void validate(String policyType, Map<String, String> parametersMap) {
        super.validate(policyType, parametersMap);
        //check particular parameters
        try {
             Float.parseFloat(parametersMap.get(FIXED_PRICE_PARAMETER));
        }catch (Exception e){
            throw new BadPolicyParametersException("The fixed plus - price policy parameters are wrong", e);
        }
    }

    @Override
    public boolean isMyType(String policyType) {
        return policyType.equalsIgnoreCase(PolicyType.FIXED_PLUS.toString());
    }

}
