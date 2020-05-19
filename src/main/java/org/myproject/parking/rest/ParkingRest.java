package org.myproject.parking.rest;

import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.User;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.service.ParkingService;
import org.myproject.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;


/**
 * Implement CRUD methods for REST service
 */

@RestController
@RequestMapping(value = "/parking/")
public class ParkingRest {

    private final Logger logger = LogManager.getRootLogger();

    @Autowired
    private ParkingService parkingService;

    @RequestMapping(value = "{id}/park", method = RequestMethod.POST)
    public ResponseEntity<ParkingSpot> parkVehicle(@PathVariable("id") Integer id, @RequestBody Vehicle vehicle) {
        logger.debug("Place a vehicle in park with id: " + id);

        ParkingSpot spot = parkingService.parkVehicle(id, vehicle);
        return new ResponseEntity<>(spot, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/leave", method = RequestMethod.POST)
    public ResponseEntity<Invoice> leaveParking(@PathVariable("id") Integer id,
                                              @ApiParam(value = "The license plate of the leaving Vehicle") @RequestParam(value = "licensePlate") String licensePlate) {
        logger.debug("A vehicle leaves the park with id: " + id);

        Invoice invoice = parkingService.leaveParking(id, licensePlate);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
}
