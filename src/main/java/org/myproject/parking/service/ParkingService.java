package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.exception.UnprocessableEntityException;
import org.myproject.parking.exception.WrongSpotStateException;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.persistence.ParkingLot;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.util.PlateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service("parkingService")
@Log4j2
@Transactional
public class ParkingService {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private PlateValidator plateValidator;

    @Autowired
    private ParkingLotService parkingLotService;

    public ParkingSpot getParkingSpotById(Integer parkingLotId, Integer spotId){
        ParkingLot parkingLot = parkingLotService.getParkingLotAndCheck(parkingLotId);
        return parkingSpotService.findSpotById(parkingLot, spotId);
    }

    public ParkingSpot parkVehicle(Integer parkingId, Vehicle vehicle) {
        plateValidator.validateLicensePlate(vehicle.getLicensePlate());

        ParkingLot parkingLot = parkingLotService.getParkingLotAndCheck(parkingId);

        ParkingSpot spotToParkVehicle = parkingSpotService.getFreeSpotInParkingByType(parkingLot, vehicle.getType());
        if (!spotToParkVehicle.placeVehicle(vehicle))
            throw new WrongSpotStateException(spotToParkVehicle.getSpotId());
        log.info("The vehicle with plate: " + vehicle.getLicensePlate() + " was parked in spot: " + spotToParkVehicle);
        log.debug("Parking state after parked car: " + parkingLot);

        parkingLotService.updateParkingLot(parkingLot);
        return spotToParkVehicle;
    }

    public Invoice leaveParking(Integer parkingLotId, Vehicle vehicle) {
        String vehiclePlate = vehicle.getLicensePlate();
        ParkingLot parkingLot = parkingLotService.getParkingLotAndCheck(parkingLotId);

        ParkingSpot spot = parkingSpotService.getSpotInParkingByVehiclePlate(parkingLot, vehiclePlate);

        if (!spot.hasCorrectType(vehicle))
            throw new UnprocessableEntityException("The spot type'" + spot.getVehicleType() +
                    "' does not match with provided vehicle type: " + vehicle.getType());

        if (!spot.setupLeavingTime())
            throw new WrongSpotStateException("The spot '" + spot.getSpotId() +
                    "' is in wrong state for corresponding vehicle with plate: " + vehiclePlate);

        log.info("The vehicle with plate: " + vehiclePlate + " is leaving from the parked spot: " + spot);
        log.debug("Parking state with leaving car: " + parkingLot);

        //billing service responsible for generating the invoice
        Invoice invoice = billingService.billVehicle(parkingLot, spot.getPrice(), spot.getSpotRent());
        spot.freeSpot();

        log.info("The invoice was generated for vehicle with license plate: " + vehiclePlate + " invoice: " + invoice);
        log.debug("Parking state after car has left: " + parkingLot);

        parkingLotService.updateParkingLot(parkingLot);
        return invoice;
    }

}
