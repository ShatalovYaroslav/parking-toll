package org.myproject.parking.service;

import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("spotService")
public class ParkingSpotService {
    //should be synchronized
    private Map<VehicleType, List<ParkingSpot>> freeSpotsByType = new HashMap<>();
    private Map<String, ParkingSpot> vehicleInSpotMap = new HashMap<>();
    private Map<String, Vehicle> vehicleByPlateMap = new HashMap<>();

    public ParkingSpot getFreeSpotByType(VehicleType type) {
        List<ParkingSpot> freeSpots = freeSpotsByType.get(type);
        if (freeSpots.isEmpty()) {
            throw new RuntimeException("No free spots for vehicle type: " + type);
        }
        return freeSpots.get(0);
    }

    public Vehicle findVehicleByPlate(String plate) {
        return vehicleByPlateMap.get(plate);
    }

    public ParkingSpot findSpotByVehiclePlate(String plate) {
        return vehicleInSpotMap.get(plate);
    }

    public ParkingSpot getFreeSpotInParkingByType(Parking parking, VehicleType type) {
        //can be optimized with cash for fast lookup
        Optional<ParkingSpot> requiredSpot = parking.getSpots().stream()
                .filter(spot -> spot.getVehicleType() == type)
                .filter(ParkingSpot::isFree).findFirst();

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException(type));
    }

    public ParkingSpot getSpotInParkingByVehiclePlate(Parking parking, String plate) {
        //can be optimized with cash for fast lookup
        Optional<ParkingSpot> requiredSpot = parking.getSpots().stream()
                .filter(spot -> spot.getSpotRent() != null)
                .filter(spot -> spot.getSpotRent().getVehicle().getLicensePlate().equals(plate)).findFirst();

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException("No parking spot found for vehicle with license plate: " + plate));
    }
}
