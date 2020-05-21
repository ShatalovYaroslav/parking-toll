package org.myproject.parking.util;

import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ParkingLotStartupFixture {
    public ParkingLotMetadata createParking(){
        Integer parkingId = 1;
        String name = "Test parking";
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams= new HashMap<>();

        String pricingPolicyType = PolicyType.STANDARD.toString();
        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);

        return new ParkingLotMetadata(parkingId, name,
                pricingPolicyType, pricingParams, priceByVehicleType, spotsNumberByType);
    }
}
