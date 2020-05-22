package org.myproject.parking.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


/**
 * Implement CRUD methods for REST service
 */

@RestController
@RequestMapping(value = "/parking/lot/")
public class ParkingLotController {

    private final Logger logger = LogManager.getRootLogger();


    @Autowired
    private ParkingLotService parkingLotService;

    @ApiOperation(value = "Create parking lot")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ParkingLot> createParkingLot(
            @ApiParam(value = "The Parking Lot Metadata to create a new parking Lot") @RequestBody ParkingLotMetadata parkingLotMetadata) {
        logger.debug("Create parking Lot with name: " + parkingLotMetadata.getName());

        ParkingLot parkingLot = parkingLotService.createParkingLot(parkingLotMetadata);
        return new ResponseEntity<>(parkingLot, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get information about the parking lot")
    @RequestMapping(value = "{parkingLotId}", method = RequestMethod.GET)
    public ResponseEntity<ParkingLot> getParkingLot(
            @ApiParam(value = "The ID of existing parking Lot") @PathVariable("parkingLotId") Integer parkingLotId) {
        logger.debug("Get parking Lot with id: " + parkingLotId);

        ParkingLot parkingLot = parkingLotService.getParkingLot(parkingLotId);
        return new ResponseEntity<>(parkingLot, HttpStatus.OK);
    }

    @ApiOperation(value = "Get information about all parking lots")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<ParkingLot>> getAllParkingLots() {
        logger.debug("Get all parking Lots ");
        List<ParkingLot> lots = parkingLotService.getAllParkingLots();
        if (lots.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lots, HttpStatus.OK);
    }
}
