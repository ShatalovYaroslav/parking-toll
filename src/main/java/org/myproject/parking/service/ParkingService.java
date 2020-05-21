package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.WrongSpotStateException;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.util.PlateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("parkingService")
@Log4j2
public class ParkingService {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private PlateValidator plateValidator;

    @Autowired
    private ParkingLotService parkingLotService;

    public ParkingSpot getParkingSpotByIDd(Integer parkingId, Integer spotId){
        Parking parking = parkingLotService.getParking(parkingId);
        return parkingSpotService.findSpotById(parking, spotId);
    }

    public ParkingSpot parkVehicle(Integer parkingId, Vehicle vehicle) {
        plateValidator.validateLicensePlate(vehicle.getLicensePlate());

        Parking parking = parkingLotService.getParking(parkingId);

        ParkingSpot freeSpot = parkingSpotService.getFreeSpotInParkingByType(parking, vehicle.getType());
        if (!freeSpot.placeVehicle(vehicle))
            throw new WrongSpotStateException(freeSpot.getSpotId());
        log.info("The vehicle with plate: " + vehicle.getLicensePlate() + " was parked in spot: " + freeSpot);
        log.debug("Parking state after parked car: " + parking);
        return freeSpot;
    }

    public Invoice leaveParking(Integer parkingId, String vehiclePlate) {
        Parking parking = parkingLotService.getParking(parkingId);

        ParkingSpot spot = parkingSpotService.getSpotInParkingByVehiclePlate(parking, vehiclePlate);
        spot.setupLeavingTime();

        log.info("The vehicle with plate: " + vehiclePlate + " is leaving from the parked spot: " + spot);
        log.debug("Parking state with leaving car: " + parking);

        //todo can be separated API call
        Invoice invoice = billingService.billVehicle(parking, spot.getPrice(), spot.getSpotRent());
        spot.freeSpot();

        log.info("The invoice was generated for vehicle with license plate: " + vehiclePlate + " invoice: " + invoice);
        log.debug("Parking state after car has left: " + parking);

        return invoice;
    }

}
