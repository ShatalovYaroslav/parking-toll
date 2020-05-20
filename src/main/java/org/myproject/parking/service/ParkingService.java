package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.ResourceNotFoundException;
import org.myproject.parking.exception.WrongSpotStateException;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.util.PlateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service("parkingService")
@Log4j2
public class ParkingService {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private PlateValidator plateValidator;

    private Map<Integer, Parking> parkingMap = new HashMap<>();

    public Parking createParking(Integer parkingId, String name,
                                 Map<VehicleType, Integer> spotsNumberByType, Map<VehicleType, Float> priceByVehicleType,
                                 PolicyType pricingPolicyType) {
        Parking parking = new Parking(parkingId, name, spotsNumberByType, priceByVehicleType, pricingPolicyType);
        parkingMap.put(parking.getParkingId(), parking);
        return parking;
    }

    public ParkingSpot parkVehicle(Integer parkingId, Vehicle vehicle) {
        plateValidator.validateLicensePlate(vehicle.getLicensePlate());

        Parking parking = Optional.ofNullable(parkingMap.get(parkingId))
                .orElseThrow(() -> new ResourceNotFoundException("No parking found for id:" + parkingId));

        ParkingSpot freeSpot = parkingSpotService.getFreeSpotInParkingByType(parking, vehicle.getType());
        if (!freeSpot.placeVehicle(vehicle))
            throw new WrongSpotStateException(freeSpot.getSpotId());
        log.info("The vehicle with plate: " + vehicle.getLicensePlate() + " was parked in spot: " + freeSpot);
        log.debug("Parking state after parked car: " + parking);
        return freeSpot;
    }

    public Invoice leaveParking(Integer parkingId, String vehiclePlate) {
        Parking parking = Optional.ofNullable(parkingMap.get(parkingId))
                .orElseThrow(() -> new ResourceNotFoundException("No parking found for id:" + parkingId));

        ParkingSpot spot = parkingSpotService.getSpotInParkingByVehiclePlate(parking, vehiclePlate);
        spot.setupLeavingTime();

        log.info("The vehicle with plate: " + vehiclePlate + " is leaving from the parked spot: " + spot);
        log.debug("Parking state with leaving car: " + parking);

        //todo can be separated API call
        Invoice invoice = billingService.billVehicle(parking.getName(), parking.getPricingPolicyType(), spot.getPrice(), spot.getSpotRent());
        spot.freeSpot();

        log.info("The invoice was generated for vehicle with license plate: " + vehiclePlate + " invoice: " + invoice);
        log.debug("Parking state after car has left: " + parking);

        return invoice;
    }

}
