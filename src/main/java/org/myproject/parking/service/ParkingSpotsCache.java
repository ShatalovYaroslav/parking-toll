package org.myproject.parking.service;

import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("parkingService")
public class ParkingSpotsCache {
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
}
