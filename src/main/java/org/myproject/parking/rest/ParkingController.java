package org.myproject.parking.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *  REST API for main parking logic
 */

@RestController
@RequestMapping(value = "/parking/lot/")
public class ParkingController {

    private final Logger logger = LogManager.getRootLogger();

    @Autowired
    private ParkingService parkingService;


    @ApiOperation(value = "Place vehicle in free spot of specified parking lot")
    @RequestMapping(value = "{parkingLotId}/place", method = RequestMethod.POST)
    public ResponseEntity<ParkingSpot> parkVehicle(
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingLotId") Integer parkingLotId,
            @ApiParam(value = "The information about vehicle that should be parked") @RequestBody Vehicle vehicle) {
        logger.debug("Place a vehicle in parking lot with id: " + parkingLotId);

        ParkingSpot spot = parkingService.parkVehicle(parkingLotId, vehicle);
        return new ResponseEntity<>(spot, HttpStatus.OK);
    }

    @ApiOperation(value = "The vehicle leaves the parking, get invoice for parked time")
    @RequestMapping(value = "{parkingLotId}/leave", method = RequestMethod.PUT)
    public ResponseEntity<Invoice> leaveParking(
            @ApiParam(value = "The ID of existing parking Lot")@PathVariable("parkingLotId") Integer parkingLotId,
            @ApiParam(value = "The license plate of the leaving Vehicle") @RequestParam(value = "licensePlate") String licensePlate) {
        logger.debug("A vehicle leaves the park with id: " + parkingLotId);

        Invoice invoice = parkingService.leaveParking(parkingLotId, licensePlate);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @ApiOperation(value = "Get parking spot by id")
    @RequestMapping(value = "{parkingLotId}/spot/{parkingSpotId}", method = RequestMethod.GET)
    public ResponseEntity<ParkingSpot> getParkingSpot(
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingLotId") Integer parkingLotId,
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingSpotId") Integer parkingSpotId) {
        logger.debug("Place a vehicle in parking lot with id: " + parkingLotId);

        ParkingSpot spot = parkingService.getParkingSpotById(parkingLotId, parkingSpotId);
        return new ResponseEntity<>(spot, HttpStatus.OK);
    }
}
