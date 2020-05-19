package org.myproject.parking.service;

import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("parkingService")
public class ParkingService {

    @Autowired
    private ParkingSpotsCache parkingSpotsCache;

    @Autowired
    private BillingService billingService;

    private Map<Integer, Parking> parkingMap = new HashMap<>();

    public Parking createParking(Map<VehicleType, Integer> spotsNumberByType, int price) {
        Parking parking = new Parking(spotsNumberByType, price);
        parkingMap.put(parking.getParkingId(), parking);
        return parking;
    }

    public ParkingSpot parkVehicle(Integer parkingId, Vehicle vehicle) {
        Parking parking = parkingMap.get(parkingId);
        //todo get parking spots free spot
        ParkingSpot freeSpot = parkingSpotsCache.getFreeSpotByType(vehicle.getType());
        if (!freeSpot.placeVehicle(vehicle))
            throw new RuntimeException("The vehicle can not be placed in parking spot :" + freeSpot.getSpotId());
        return freeSpot;
    }

    public Invoice leaveParking(Integer parkingId, String vehiclePlate) {
        Parking parking = parkingMap.get(parkingId);
        //todo get parking spots spot by parkingId
        ParkingSpot spot = parkingSpotsCache.findSpotByVehiclePlate(vehiclePlate);
        spot.setupLeavingTime();

        //todo can be separated API call
        Invoice invoice = billingService.billVehicle(parking.getPricingPolicyType(), spot.getPrice(), spot.getSpotRent());
        spot.freeSpot();

        return invoice;
    }

}
