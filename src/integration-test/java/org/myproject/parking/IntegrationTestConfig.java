
package org.myproject.parking;

import com.google.common.collect.Lists;

import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.myproject.parking.pricing.StandardPricingPolicy;
import org.myproject.parking.service.BillingService;
import org.myproject.parking.service.ParkingLotService;
import org.myproject.parking.service.ParkingService;
import org.myproject.parking.service.ParkingSpotService;
import org.myproject.parking.util.PlateValidator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;



@EnableAutoConfiguration
@EntityScan(basePackages = { "org.myproject.parking" })
@Profile("test")
public class IntegrationTestConfig {

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
        PricingPoliciesFromSpringContext.add(new StandardPricingPolicy());
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
}
