package org.myproject.parking.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.persistence.ParkingSpot;
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
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Vehicle was successfully parked"),
            @ApiResponse(code = 403, message = "Forbidden to park in occupied parking spot"),
            @ApiResponse(code = 404, message = "Parking Lot or required Spot not found"),
            @ApiResponse(code = 422, message = "Unprocessable Vehicle Entity") })
    @RequestMapping(value = "{parkingLotId}/place", method = RequestMethod.POST)
    public ResponseEntity<ParkingSpot> parkVehicle(
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingLotId") Integer parkingLotId,
            @ApiParam(value = "The information about vehicle that should be parked") @RequestBody Vehicle vehicle) {
        logger.debug("Place a vehicle in parking lot with id: " + parkingLotId);

        ParkingSpot spot = parkingService.parkVehicle(parkingLotId, vehicle);
        return new ResponseEntity<>(spot, HttpStatus.OK);
    }

    @ApiOperation(value = "The vehicle leaves the parking, get invoice for parked time")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Vehicle successfully left parking, invoice is created"),
            @ApiResponse(code = 403, message = "Forbidden to leave the free parking spot"),
            @ApiResponse(code = 404, message = "Parking Lot or required Spot not found"),
            @ApiResponse(code = 422, message = "Unprocessable Vehicle Entity"),
            @ApiResponse(code = 500, message = "Wrong spot rent state") })
    @RequestMapping(value = "{parkingLotId}/leave", method = RequestMethod.PUT)
    public ResponseEntity<Invoice> leaveParking(
            @ApiParam(value = "The ID of existing parking Lot")@PathVariable("parkingLotId") Integer parkingLotId,
            @ApiParam(value = "The information about vehicle that should leave the parking")  @RequestBody Vehicle vehicle) {
        logger.debug("A vehicle leaves the park with id: " + parkingLotId);

        Invoice invoice = parkingService.leaveParking(parkingLotId, vehicle);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @ApiOperation(value = "Get parking spot by id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Get successfully required Spot"),
            @ApiResponse(code = 404, message = "Parking Lot or required Spot not found")})
    @RequestMapping(value = "{parkingLotId}/spot/{parkingSpotId}", method = RequestMethod.GET)
    public ResponseEntity<ParkingSpot> getParkingSpot(
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingLotId") Integer parkingLotId,
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingSpotId") Integer parkingSpotId) {
        logger.debug("Place a vehicle in parking lot with id: " + parkingLotId);

        ParkingSpot spot = parkingService.getParkingSpotById(parkingLotId, parkingSpotId);
        return new ResponseEntity<>(spot, HttpStatus.OK);
    }
}
