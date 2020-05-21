package org.myproject.parking.pricing;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.BadPolicyParametersException;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@ToString
@Log4j2
public class FixedPricePlusPolicy implements PricingPolicy {

    public static final String FIXED_PRICE_PARAMETER = "fixedPrice";

    @Override
    public float getPrice(int hours, float price, Map<String, String> pricingParameter) {
        try {
            float fixedPrice = Float.parseFloat(pricingParameter.get(FIXED_PRICE_PARAMETER));
            return fixedPrice + hours * price;
        }catch (Exception e){
            String errorMsg = "The price policy parameters are wrong";
            log.error(errorMsg, e);
            throw new BadPolicyParametersException(errorMsg, e);
        }
    }

    @Override
    public PolicyType getType() {
        return PolicyType.FIXED_PLUS;
    }

    @Override
    public Set<String> getRequiredParameters(){
        Set<String> params = new HashSet<>();
        params.add(FIXED_PRICE_PARAMETER);
        return params;
    }
}
