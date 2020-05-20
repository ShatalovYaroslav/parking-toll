
package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.model.Parking;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;


@Log4j2
@Component
public class ParkingStartupAdder {
    @Autowired
    ParkingService parkingService;

    @Value("${create.testing.parking}")
    private boolean createParking;

    @PostConstruct
    public void initAddParking( ) {
        log.info("Trying to add test parking on startup");
        if(!createParking){
            log.info("No test parking will be added on startup");
            return;
        }
        Integer parkingId = 1;
        String name = "Test parking";
        Map< VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();

        PolicyType pricingPolicyType = PolicyType.STANDARD;
        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);

        Parking parking = parkingService.createParking(parkingId, name,
                spotsNumberByType, priceByVehicleType,
                pricingPolicyType);
        log.info("Created test parking: " + parking);
    }
}
