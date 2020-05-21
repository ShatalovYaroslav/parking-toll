package org.myproject.parking.rest;

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
 * Implement CRUD methods for REST service
 */

@RestController
@RequestMapping(value = "/parking/")
public class ParkingRest {

    private final Logger logger = LogManager.getRootLogger();

    @Autowired
    private ParkingService parkingService;

    @RequestMapping(value = "{parkId}/place", method = RequestMethod.POST)
    public ResponseEntity<ParkingSpot> parkVehicle(@PathVariable("parkId") Integer parkingId, @RequestBody Vehicle vehicle) {
        logger.debug("Place a vehicle in park with id: " + parkingId);

        ParkingSpot spot = parkingService.parkVehicle(parkingId, vehicle);
        return new ResponseEntity<>(spot, HttpStatus.OK);
    }

    @RequestMapping(value = "{parkId}/leave", method = RequestMethod.PUT)
    public ResponseEntity<Invoice> leaveParking(@PathVariable("parkId") Integer parkId,
                                              @ApiParam(value = "The license plate of the leaving Vehicle") @RequestParam(value = "licensePlate") String licensePlate) {
        logger.debug("A vehicle leaves the park with id: " + parkId);

        Invoice invoice = parkingService.leaveParking(parkId, licensePlate);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
}
