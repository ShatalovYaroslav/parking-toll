
package org.myproject.parking.service;

import org.myproject.parking.model.*;
import org.myproject.parking.pricing.PricingPolicy;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service("billingService")
public class BillingService {

    public Invoice billVehicle(Parking parking, final ParkingSpot parkingSpot) {
        PricingPolicy pricingPolicy = parking.getPricingPolicy();

        SpotRent spotRent = parkingSpot.getSpotRent();

        int rentHours = getParkingDurationInHours(spotRent);
        float cost = pricingPolicy.getPrice(rentHours, parkingSpot.getPrice());

        return new Invoice(spotRent, cost);
    }

    public int getParkingDurationInHours(SpotRent spotRent) {
       return (int)Duration.between(spotRent.getArrivalTime(), spotRent.getLeavingTime()).toHours();
    }

}
