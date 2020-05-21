
package org.myproject.parking.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.parking.IntegrationTestConfig;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.util.ParkingLotStartupFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static com.google.common.truth.Truth.assertThat;
import static org.myproject.parking.IntegrationTestConfig.FIXED_PRICE_POLICY;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfig.class})
public class ParkingLogicIntegrationTest {

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    ParkingLotStartupFixture parkingLotStartupFixture;

    private Parking parking;

    @Before
    public void createParking() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();

        parking = parkingLotService.createParking(parkingLotMetadata);

        assertThat(parking).isNotNull();
        assertThat(parking.getName()).isEqualTo(parkingLotMetadata.getName());
        assertThat(parking.getSpots().size()).isEqualTo(7);
    }

    @After
    public void deleteParking() {
        parkingLotService.removeParking(parking.getParkingId());
    }

    @Test
    public void testParkVehicle() {
        LocalDateTime startParking = LocalDateTime.now();
        Vehicle testCar = new Sedan("license plate 1");
        ParkingSpot parkedSpot = parkingService.parkVehicle(parking.getParkingId(), testCar);

        assertThat(parkedSpot.isFree()).isFalse();
        assertThat(parkedSpot.getSpotRent()).isNotNull();
        assertThat(parkedSpot.getSpotRent().getArrivalTime()).isAtLeast(startParking);
        assertThat(parkedSpot.getSpotRent().getVehicle()).isEqualTo(testCar);
    }

    @Test(expected = SpotNotFoundException.class)
    public void tesVehicleLeavesParkingWithoutBeingParked() {
        Vehicle testCar = new Sedan("license plate 1");
        Invoice invoice = parkingService.leaveParking(parking.getParkingId(), testCar.getLicensePlate());
    }

    @Test
    public void tesVehicleLeavesParking() {
        LocalDateTime startParking = LocalDateTime.now();
        Vehicle testCar = new Sedan("license plate 1");
        //place vehicle first
        ParkingSpot parkedSpot = parkingService.parkVehicle(parking.getParkingId(), testCar);

        //leave parking and get the Invoice
        Invoice invoice = parkingService.leaveParking(parking.getParkingId(), testCar.getLicensePlate());
        assertThat(invoice).isNotNull();
        assertThat(invoice.getParkingName()).isEqualTo(parking.getName());
        assertThat(invoice.getLicensePlate()).isEqualTo(testCar.getLicensePlate());
        assertThat(invoice.getArrivalTime()).isAtLeast(startParking);
        assertThat(invoice.getLeavingTime()).isNotNull();
        assertThat(invoice.getLeavingTime()).isAtLeast(invoice.getArrivalTime());
        assertThat(invoice.getCost()).isEqualTo(FIXED_PRICE_POLICY);

        //check that parking has the same amount of spots
        Parking parkingAfterLeave = parkingLotService.getParking(parking.getParkingId());
        assertThat(parkingAfterLeave.getSpots().size()).isEqualTo(parking.getSpots().size());

        //check if spot is free
        ParkingSpot spotFreed = parkingService.getParkingSpotByIDd(parking.getParkingId(), parkedSpot.getSpotId());
        assertThat(spotFreed).isNotNull();
        assertThat(spotFreed.getSpotRent()).isNull();
        assertThat(spotFreed.isFree()).isTrue();
    }
}
