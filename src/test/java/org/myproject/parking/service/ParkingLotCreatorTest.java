package org.myproject.parking.service;

import org.junit.Before;
import org.junit.Test;
import org.myproject.parking.exception.AddParkingException;
import org.myproject.parking.model.persistence.ParkingLot;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.persistence.PricingConfig;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.pricing.PolicyType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.myproject.parking.pricing.policy.FixedPricePlusPolicy.FIXED_PRICE_PARAMETER;

public class ParkingLotCreatorTest {

    private ParkingLotCreator parkingLotCreator;

    @Before
    public void init() {
        parkingLotCreator = new ParkingLotCreator();
    }

    @Test
    public void testValidateCorrectParameters() throws Exception {
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams = new HashMap<>();
        pricingParams.put(FIXED_PRICE_PARAMETER, "10.0");

        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);
        parkingLotCreator.validateParameters(spotsNumberByType, priceByVehicleType);
    }

    @Test(expected = AddParkingException.class)
    public void testValidateWrongValueParameters() throws Exception {
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams = new HashMap<>();
        pricingParams.put(FIXED_PRICE_PARAMETER, "10.0");

        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        parkingLotCreator.validateParameters(spotsNumberByType, priceByVehicleType);
    }

    @Test(expected = AddParkingException.class)
    public void testValidateWrongSizeParameters() throws Exception {
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();

        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        parkingLotCreator.validateParameters(spotsNumberByType, priceByVehicleType);
    }

    @Test(expected = AddParkingException.class)
    public void testValidateNullParameters() throws Exception {
        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = null;
        parkingLotCreator.validateParameters(spotsNumberByType, priceByVehicleType);
    }

    @Test
    public void testPopulateParkingWithSpots() throws Exception {
        String lotName = "lot name";

        Map<VehicleType, Integer> spotsNumberByType = new HashMap<>();
        Map<VehicleType, Float> priceByVehicleType = new HashMap<>();
        Map<String, String> pricingParams = new HashMap<>();
        PricingConfig pricingConfig = new PricingConfig(1, PolicyType.FIXED_PLUS, pricingParams);
        pricingParams.put(FIXED_PRICE_PARAMETER, "10.0");

        spotsNumberByType.put(VehicleType.GASOLINE, 3);
        spotsNumberByType.put(VehicleType.FIFTY_KW, 2);
        spotsNumberByType.put(VehicleType.TWENTY_KW, 2);

        priceByVehicleType.put(VehicleType.GASOLINE, 10.0f);
        priceByVehicleType.put(VehicleType.FIFTY_KW, 7.0f);
        priceByVehicleType.put(VehicleType.TWENTY_KW, 5.2f);

        ParkingLot parkingLot = parkingLotCreator.populateParkingWithSpots(
                lotName, spotsNumberByType, priceByVehicleType, pricingConfig);
        assertEquals(parkingLot.getName(), lotName);
        assertEquals(parkingLot.getSpots().size(), 7);
        assertEquals(parkingLot.getPricingConfig().getPolicyType(), pricingConfig.getPolicyType());
        assertEquals(parkingLot.getPricingConfig().getPricingParameters(), pricingConfig.getPricingParameters());

        for(ParkingSpot spot : parkingLot.getSpots()) {
            assertTrue(spot.isFree());
        }
    }
}
