
package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.util.ParkingLotStartupFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Log4j2
@Component
public class ParkingStartupAdder {
    @Autowired
    ParkingLotService parkingLotService;

    @Autowired
    ParkingLotStartupFixture parkingLotStartupFixture;

    @Value("${create.testing.parking}")
    private boolean createParking;

    @PostConstruct
    public void initAddParking( ) {
        log.info("Trying to add test parking on startup");
        if(!createParking){
            log.info("No test parking will be added on startup");
            return;
        }

        ParkingLot parkingLot = parkingLotService.createParkingLot(parkingLotStartupFixture.createParking());
        log.info("Created test parking lot: " + parkingLot);
    }


}
