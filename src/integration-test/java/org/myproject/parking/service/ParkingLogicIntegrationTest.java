
package org.myproject.parking.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.parking.IntegrationTestConfig;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.ParkingLotMetadata;
import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.persistence.ParkingLot;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.util.ParkingLotStartupFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static com.google.common.truth.Truth.assertThat;
import static org.myproject.parking.IntegrationTestConfig.FIXED_PRICE_POLICY;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfig.class})
@EntityScan("org.ow2.proactive.cloud_watch.repository")
public class ParkingLogicIntegrationTest {

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    ParkingLotStartupFixture parkingLotStartupFixture;

    private ParkingLot parkingLot;

    @Before
    public void createParking() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();

        parkingLot = parkingLotService.createParkingLot(parkingLotMetadata);

        assertThat(parkingLot).isNotNull();
        assertThat(parkingLot.getName()).isEqualTo(parkingLotMetadata.getName());
        assertThat(parkingLot.getSpots().size()).isEqualTo(7);
    }

    @After
    public void deleteParking() {
        parkingLotService.removeParkingLot(parkingLot.getParkingLotId());
    }

    @Test
    public void testParkVehicle() {
        LocalDateTime startParking = LocalDateTime.now();
        Vehicle testCar = new Sedan("license plate 1");
        ParkingSpot parkedSpot = parkingService.parkVehicle(parkingLot.getParkingLotId(), testCar);

        assertThat(parkedSpot.isFree()).isFalse();
        assertThat(parkedSpot.getSpotRent()).isNotNull();
        assertThat(parkedSpot.getSpotRent().getArrivalTime()).isAtLeast(startParking);
        assertThat(parkedSpot.getSpotRent().getVehiclePlate()).isEqualTo(testCar.getLicensePlate());
    }

    @Test(expected = SpotNotFoundException.class)
    public void tesVehicleLeavesParkingWithoutBeingParked() {
        Vehicle testCar = new Sedan("license plate 1");
        Invoice invoice = parkingService.leaveParking(parkingLot.getParkingLotId(), testCar.getLicensePlate());
    }

    @Test
    public void tesVehicleLeavesParking() {
        LocalDateTime startParking = LocalDateTime.now();
        Vehicle testCar = new Sedan("license plate 1");
        //place vehicle first
        ParkingSpot parkedSpot = parkingService.parkVehicle(parkingLot.getParkingLotId(), testCar);

        //leave parking and get the Invoice
        Invoice invoice = parkingService.leaveParking(parkingLot.getParkingLotId(), testCar.getLicensePlate());
        assertThat(invoice).isNotNull();
        assertThat(invoice.getParkingName()).isEqualTo(parkingLot.getName());
        assertThat(invoice.getLicensePlate()).isEqualTo(testCar.getLicensePlate());
        assertThat(invoice.getArrivalTime()).isAtLeast(startParking);
        assertThat(invoice.getLeavingTime()).isNotNull();
        assertThat(invoice.getLeavingTime()).isAtLeast(invoice.getArrivalTime());
        assertThat(invoice.getCost()).isEqualTo(FIXED_PRICE_POLICY);

        //check that parking has the same amount of spots
        ParkingLot parkingLotAfterLeave = parkingLotService.getParkingLotAndCheck(parkingLot.getParkingLotId());
        assertThat(parkingLotAfterLeave.getSpots().size()).isEqualTo(parkingLot.getSpots().size());

        //check if spot is free
        ParkingSpot spotFreed = parkingService.getParkingSpotById(parkingLot.getParkingLotId(), parkedSpot.getSpotId());
        assertThat(spotFreed).isNotNull();
        assertThat(spotFreed.getSpotRent()).isNull();
        assertThat(spotFreed.isFree()).isTrue();
    }
}
