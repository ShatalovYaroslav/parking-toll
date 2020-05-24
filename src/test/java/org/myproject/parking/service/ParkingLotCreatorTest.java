
package org.myproject.parking.service;

import org.junit.Before;
import org.junit.Test;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.myproject.parking.pricing.policy.FixedPricePlusPolicy;
import org.myproject.parking.pricing.policy.StandardPricingPolicy;
import org.myproject.parking.pricing.provider.DefaultPriceConfigProvider;
import org.myproject.parking.pricing.provider.FixedPlusPriceConfigProvider;
import org.myproject.parking.pricing.provider.PricingConfigProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class ParkingLotCreatorTest {


    private ParkingLotCreator parkingLotCreator;

    private List<PricingConfigProvider> pricingConfigProviders;

    private PricingPolicyCatalog pricingPolicyCatalog;

    @Before
    public void init() {

        List<PricingPolicy> mockPricingPolicyList = new ArrayList<>();
        mockPricingPolicyList.add(new FixedPricePlusPolicy());
        mockPricingPolicyList.add(new StandardPricingPolicy());

        pricingPolicyCatalog = new PricingPolicyCatalog(mockPricingPolicyList);

        pricingConfigProviders = new ArrayList<>();
        pricingConfigProviders.add(new DefaultPriceConfigProvider(pricingPolicyCatalog));
        pricingConfigProviders.add(new FixedPlusPriceConfigProvider());
        parkingLotCreator = new ParkingLotCreator(pricingConfigProviders);
    }

    @Test
    public void testParkVehicle() {
        ParkingLotMetadata parkingLotMetadata = createTestParking();

        PricingPolicy pricingPolicy = new FixedPricePlusPolicy();
         /*
        Optional<PricingConfigProvider> pricingConfigProvider = Optional.of(new DefaultPriceConfigProvider());
        when(pricingConfigProviders.stream().filter(any())
                .findFirst())
                .thenReturn(pricingConfigProvider);


        when(pricingPolicyCatalog.getPolicyByPolicyTypeAsString(anyString()))
                .thenReturn(pricingPolicy);

          */

        ParkingLot parkingLot = parkingLotCreator.createParking(parkingLotMetadata);

        assertEquals(parkingLot.getName(), parkingLotMetadata.getName());
        assertEquals(parkingLot.getPricingConfig().getPolicyType(), PolicyType.STANDARD);
    }

    private ParkingLotMetadata createTestParking(){
        String name = "Test parking";
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams = new HashMap<>();
        //pricingParams.put(FIXED_PRICE_PARAMETER, "10.0");

        String pricingPolicyType = PolicyType.STANDARD.toString();
        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);

        return new ParkingLotMetadata(name,
                pricingPolicyType, pricingParams, priceByVehicleType, spotsNumberByType);
    }

}
