
package org.myproject.parking.service;

import java.util.*;

import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.User;
import org.myproject.parking.model.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("parkingService")
public class ParkingService {

    @Autowired
    private ParkingSpotsCache parkingSpotsCache;

    public ParkingSpot parkVehicle(Vehicle vehicle) {
        ParkingSpot freeSpot = parkingSpotsCache.getFreeSpotByType(vehicle.getType());
        if(!freeSpot.placeVehicle(vehicle))
            throw new RuntimeException("The vehicle can not be placed in parking spot :" + freeSpot.getSpotId());
        return freeSpot;
    }

    public Invoice leaveParking(String vehiclePlate) {
        ParkingSpot spot = parkingSpotsCache.findSpotByVehiclePlate(vehiclePlate);
        spot.setupLeavingTime();
        //todo billing service get bill
        spot.freeSpot();
        return new Invoice();
    }

}
