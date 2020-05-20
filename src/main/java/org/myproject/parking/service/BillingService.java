package org.myproject.parking.service;

import org.myproject.parking.exception.WrongSpotRentException;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.SpotRent;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service("billingService")
public class BillingService {

    @Autowired
    private PricingPolicyCatalog pricingPolicyCatalog;

    public Invoice billVehicle(String parkingName, PolicyType policyType, float price, final SpotRent spotRent) {
        validateSpotRent(spotRent);
        PricingPolicy pricingPolicy = pricingPolicyCatalog.getPolicyByPolicyType(policyType);

        int rentHours = getParkingDurationInHours(spotRent);
        float cost = pricingPolicy.getPrice(rentHours, price);

        return new Invoice(parkingName, spotRent, cost);
    }

    public int getParkingDurationInHours(SpotRent spotRent) {
        return (int) Duration.between(spotRent.getArrivalTime(), spotRent.getLeavingTime()).toHours();
    }

    private void validateSpotRent(final SpotRent spotRent){
        if(spotRent.getVehicle() == null){
            throw new WrongSpotRentException("There is no vehicle placed for this spot Rental: " + spotRent);
        }
        if(spotRent.getArrivalTime() == null || spotRent.getLeavingTime() == null){
            throw new WrongSpotRentException("The arrival or leaving time is not setup for this spot Rental: " + spotRent);
        }
        if(spotRent.getArrivalTime().isAfter(spotRent.getLeavingTime())){
            throw new WrongSpotRentException("The arrival is after the leaving time for this spot Rental: " + spotRent);
        }
    }

}
