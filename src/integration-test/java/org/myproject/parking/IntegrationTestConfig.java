
package org.myproject.parking;

import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.myproject.parking.pricing.policy.FixedPricePlusPolicy;
import org.myproject.parking.pricing.policy.StandardPricingPolicy;
import org.myproject.parking.pricing.provider.DefaultPriceConfigProvider;
import org.myproject.parking.pricing.provider.FixedPlusPriceConfigProvider;
import org.myproject.parking.pricing.provider.PricingConfigProvider;
import org.myproject.parking.service.*;
import org.myproject.parking.util.ParkingLotStartupFixture;
import org.myproject.parking.util.PlateValidator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;



@EnableAutoConfiguration
@EntityScan(basePackages = { "org.myproject.parking" })
@Profile("test")
@PropertySource("classpath:application-test.properties")
public class IntegrationTestConfig {

    public static final float FIXED_PRICE_POLICY = 10f;

    @Bean
    public DataSource testDataSource() {
        return createMemDataSource();
    }

    private DataSource createMemDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).build();
        return db;
    }

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
        List<PricingPolicy> pricingPolicies = new ArrayList<>();
        pricingPolicies.add(new FixedPricePlusPolicy());
        pricingPolicies.add(new StandardPricingPolicy());
        return new PricingPolicyCatalog(pricingPolicies);
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

    @Bean
    public ParkingLotCreator parkingLotCreator() {
        List<PricingConfigProvider> pricingConfigProviders = new ArrayList<>();
        pricingConfigProviders.add(new DefaultPriceConfigProvider());
        pricingConfigProviders.add(new FixedPlusPriceConfigProvider());
        return new ParkingLotCreator(pricingConfigProviders);
    }

    @Bean
    public ParkingLotStartupFixture parkingLotStartupFixture() {
        return new ParkingLotStartupFixture();
    }

    @Bean
    public PricingConfigProvider pricingConfigProvider() {
        return new FixedPlusPriceConfigProvider();
    }
}
