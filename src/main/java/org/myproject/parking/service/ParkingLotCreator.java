package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.exception.AddParkingException;
import org.myproject.parking.exception.BadPolicyParametersException;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.pricing.PricingConfig;
import org.myproject.parking.pricing.provider.PricingConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class ParkingLotCreator {

    @Autowired
    private List<PricingConfigProvider> pricingConfigProviders;

    public ParkingLot createParking(ParkingLotMetadata parkingLotMetadata) {
        //it will be called specific class according to policyType name
        PricingConfigProvider pricingConfigProvider = pricingConfigProviders.stream()
                .filter(configProvider -> configProvider.isMyType(parkingLotMetadata.getPolicyType()))
                .findFirst()
                .orElseThrow(() -> new BadPolicyParametersException("No pricing policy for type: " + parkingLotMetadata.getPolicyType() +
                        ", the possible types are: " + EnumSet.allOf(PolicyType.class)));

        PricingConfig pricingConfig = pricingConfigProvider.validateAndGetPriceConfig(parkingLotMetadata.getPolicyType(),
                parkingLotMetadata.getPricingParameters());

        validateParameters(parkingLotMetadata.getSpotsNumberByType(), parkingLotMetadata.getPriceByVehicleType(), pricingConfig.getPolicyType());

        return populateParkingWithSpots(parkingLotMetadata.getParkingId(),
                parkingLotMetadata.getName(), parkingLotMetadata.getSpotsNumberByType(), parkingLotMetadata.getPriceByVehicleType(), pricingConfig);
    }

    private ParkingLot populateParkingWithSpots(Integer parkId, String name, Map<VehicleType, Integer> spotsNumberByType, Map<VehicleType, Float> priceByVehicleType, PricingConfig pricingConfig) {
        List<ParkingSpot> spots = new ArrayList<>();

        int i = 1;
        for (Map.Entry<VehicleType, Integer> ent : spotsNumberByType.entrySet()) {
            for (int c = 0; c < ent.getValue(); c++) {
                float price = priceByVehicleType.get(ent.getKey());
                // spotId will be autoincrement in DB
                spots.add(new ParkingSpot(i++, ent.getKey(), price));
            }
        }
        return new ParkingLot(parkId, name, spots, pricingConfig);
    }

    protected void validateParameters(Map<VehicleType, Integer> spotsNumberByType, Map<VehicleType, Float> priceByVehicleType, PolicyType policyType) {
        if (spotsNumberByType.size() != priceByVehicleType.size() ||
                    !spotsNumberByType.keySet().equals(priceByVehicleType.keySet())) {
            throw new AddParkingException("The values of Vehicle Types for spots do not match with the Vehicle Types for pricing");
        }
    }
}
