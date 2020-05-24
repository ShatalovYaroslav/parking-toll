
package org.myproject.parking.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.myproject.parking.pricing.policy.FixedPricePlusPolicy.FIXED_PRICE_PARAMETER;


public class ParkingLotCreatorTest2 {

    @InjectMocks
    private ParkingLotCreator parkingLotCreator;

    @Mock
    private List<PricingConfigProvider> pricingConfigProviders;

    @Mock
    private PricingPolicyCatalog pricingPolicyCatalog;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(DefaultPriceConfigProvider.class);
       // pricingConfigProviders = new ArrayList<>();
       // pricingConfigProviders.add(new DefaultPriceConfigProvider());
       // pricingConfigProviders.add(new FixedPlusPriceConfigProvider());
       // parkingLotCreator = new ParkingLotCreator(pricingConfigProviders);
    }

    @Test
    public void testParkVehicle() {
        ParkingLotMetadata parkingLotMetadata = createTestParking();

        List<PricingPolicy> mockPricingPolicyList = new ArrayList<>();
        mockPricingPolicyList.add(new FixedPricePlusPolicy());
        mockPricingPolicyList.add(new StandardPricingPolicy());

        PricingPolicyCatalog mockPricingPolicyCatalog = new PricingPolicyCatalog(mockPricingPolicyList);

         List<PricingConfigProvider> mockPricingConfigProviders = new ArrayList<>();
        mockPricingConfigProviders.add(new DefaultPriceConfigProvider(mockPricingPolicyCatalog));
        mockPricingConfigProviders.add(new FixedPlusPriceConfigProvider());
        Stream<PricingConfigProvider> streamMock=  mockPricingConfigProviders.stream();
        when(pricingConfigProviders.stream())
                .thenReturn(streamMock);


        PricingPolicy pricingPolicy = new FixedPricePlusPolicy();
         /*
        Optional<PricingConfigProvider> pricingConfigProvider = Optional.of(new DefaultPriceConfigProvider());
        when(pricingConfigProviders.stream().filter(any())
                .findFirst())
                .thenReturn(pricingConfigProvider);

         */
        when(pricingPolicyCatalog.getPolicyByPolicyTypeAsString(anyString()))
                .thenReturn(pricingPolicy);


        ParkingLot parkingLot = parkingLotCreator.createParking(parkingLotMetadata);

        assertEquals(parkingLot.getName(), parkingLotMetadata.getName());
        assertEquals(parkingLot.getPricingConfig().getPolicyType(), PolicyType.FIXED_PLUS);
        assertEquals(parkingLot.getPricingConfig()
                .getPricingParameters().get(FIXED_PRICE_PARAMETER), "10.0");
    }

    private ParkingLotMetadata createTestParking(){
        String name = "Test parking";
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams = new HashMap<>();
        pricingParams.put(FIXED_PRICE_PARAMETER, "10.0");

        String pricingPolicyType = PolicyType.FIXED_PLUS.toString();
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
