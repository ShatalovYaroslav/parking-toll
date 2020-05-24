package org.myproject.parking.util;

import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.myproject.parking.pricing.policy.FixedPricePlusPolicy.FIXED_PRICE_PARAMETER;

@Component
public class ParkingLotStartupFixture {
    public ParkingLotMetadata createParking(){
        String name = "Test parking";
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams = new HashMap<>();
        pricingParams.put(FIXED_PRICE_PARAMETER, "10.0");

        String pricingPolicyType = PolicyType.FIXED_PLUS.toString();
        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);

        return new ParkingLotMetadata(name,
                pricingPolicyType, pricingParams, priceByVehicleType, spotsNumberByType);
    }
}
