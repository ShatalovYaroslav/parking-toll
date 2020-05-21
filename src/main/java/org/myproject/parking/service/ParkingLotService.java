package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.ResourceNotFoundException;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class ParkingLotService {
    private Map<Integer, Parking> parkingMap = new HashMap<>();

    public Parking createParking(Integer parkingId, String name,
                                 Map<VehicleType, Integer> spotsNumberByType, Map<VehicleType, Float> priceByVehicleType,
                                 PolicyType pricingPolicyType) {
        Parking parking = new Parking(parkingId, name, spotsNumberByType, priceByVehicleType, pricingPolicyType);
        parkingMap.put(parking.getParkingId(), parking);
        return parking;
    }

    public Parking getParking(Integer parkingId) {
        Parking parking = Optional.ofNullable(parkingMap.get(parkingId))
                .orElseThrow(() -> new ResourceNotFoundException("No parking found for id:" + parkingId));
        return parking;
    }

    public void removeParking(Integer parkingId) {
        getParking(parkingId);
        parkingMap.remove(parkingId);
    }
}
