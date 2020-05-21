
package org.myproject.parking.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.parking.IntegrationTestConfig;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfig.class})

public class ParkingLogicIntegrationTest {

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private ParkingService parkingService;

    private Parking parking;

    @Before
    public void createBucket() {
        Integer parkingId = 1;
        String name = "Test parking";
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();

        PolicyType pricingPolicyType = PolicyType.STANDARD;
        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);

        parking = parkingLotService.createParking(parkingId, name,
                spotsNumberByType, priceByVehicleType,
                pricingPolicyType);

        assertThat(parking).isNotNull();
        assertThat(parking.getName()).isEqualTo(name);
        assertThat(parking.getSpots().size()).isEqualTo(7);
    }

    @After
    public void deleteBucket() {
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
}
