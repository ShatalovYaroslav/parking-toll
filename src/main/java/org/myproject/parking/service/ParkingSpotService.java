package org.myproject.parking.service;

import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("spotService")
public class ParkingSpotService {

    public ParkingSpot findSpotById(ParkingLot parkingLot, int spotId) {
        Optional<ParkingSpot> requiredSpot = parkingLot.getSpots().stream()
                .filter(spot -> spot.getSpotId() == spotId)
                .filter(ParkingSpot::isFree).findFirst();

        return requiredSpot.orElseThrow(SpotNotFoundException::new);
    }

    public ParkingSpot getFreeSpotInParkingByType(ParkingLot parkingLot, VehicleType type) {
        //can be optimized with cash for fast lookup
        Optional<ParkingSpot> requiredSpot = parkingLot.getSpots().stream()
                .filter(spot -> spot.getVehicleType() == type)
                .filter(ParkingSpot::isFree).findFirst();

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException(type));
    }

    public ParkingSpot getSpotInParkingByVehiclePlate(ParkingLot parkingLot, String plate) {
        //can be optimized with cash for fast lookup
        Optional<ParkingSpot> requiredSpot = parkingLot.getSpots().stream()
                .filter(spot -> spot.getSpotRent() != null)
                .filter(spot -> spot.getSpotRent().getVehicle().getLicensePlate().equals(plate)).findFirst();

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException("No parking spot found for vehicle with license plate: " + plate));
    }
}
