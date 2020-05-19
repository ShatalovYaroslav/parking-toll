package org.myproject.parking.service;

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

    public Invoice billVehicle(PolicyType policyType, float price, final SpotRent spotRent) {
        PricingPolicy pricingPolicy = pricingPolicyCatalog.getPolicyByPolicyType(policyType);

        int rentHours = getParkingDurationInHours(spotRent);
        float cost = pricingPolicy.getPrice(rentHours, price);

        return new Invoice(spotRent, cost);
    }

    public int getParkingDurationInHours(SpotRent spotRent) {
        return (int) Duration.between(spotRent.getArrivalTime(), spotRent.getLeavingTime()).toHours();
    }

}
