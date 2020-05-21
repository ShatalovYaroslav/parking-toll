
package org.myproject.parking;

import org.myproject.parking.pricing.FixedPricePlusPolicy;
import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.myproject.parking.service.*;
import org.myproject.parking.util.PlateValidator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;



@EnableAutoConfiguration
@EntityScan(basePackages = { "org.myproject.parking" })
@Profile("test")
public class IntegrationTestConfig {

    public static final float FIXED_PRICE_POLICY = 10f;

    @Bean
    public ParkingService parkingService() {
        return new ParkingService();
    }

    @Bean
    public ParkingLotService parkingLotService() {
        return new ParkingLotService();
    }

    @Bean
    public BillingService BillingService() {
        return new BillingService();
    }

    @Bean
    public PricingPolicyCatalog PricingPolicyCatalog() {
        List<PricingPolicy> PricingPoliciesFromSpringContext = new ArrayList<>();
        PricingPoliciesFromSpringContext.add(new FixedPricePlusPolicy());
        return new PricingPolicyCatalog(PricingPoliciesFromSpringContext);
    }
    @Bean
    public ParkingSpotService ParkingSpotService() {
        return new ParkingSpotService();
    }

    @Bean
    public PlateValidator plateValidator() {
        return new PlateValidator();
    }

    @Bean
    public ParkingStartupAdder parkingStartupAdder() {
        return new ParkingStartupAdder();
    }
}
