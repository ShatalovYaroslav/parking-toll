
package org.myproject.parking.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.myproject.parking.dto.Invoice;
import org.myproject.parking.exception.WrongSpotRentException;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.model.PricingConfig;
import org.myproject.parking.model.SpotRent;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.pricing.PricingPolicy;
import org.myproject.parking.pricing.PricingPolicyCatalog;
import org.myproject.parking.pricing.policy.StandardPricingPolicy;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BillingServiceTest {

    @InjectMocks
    private BillingService billingService;

    @Mock
    private PricingPolicyCatalog pricingPolicyCatalog;

    @Test
    public void testBillVehicle() throws Exception {
        String plate = "plate";
        float price = 4;
        int rentHours = 2;
        LocalDateTime arrivalTime = LocalDateTime.now();
        LocalDateTime leavingTime = LocalDateTime.now().plusHours(rentHours);

        PricingPolicy pricingPolicy = new StandardPricingPolicy();
        when(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.STANDARD))
                .thenReturn(pricingPolicy);

        PricingConfig pricingConfig = new PricingConfig(1, PolicyType.STANDARD, new HashMap<>());
        ParkingLot mockedLot = ParkingLot.builder()
                .parkingLotId(4).name("parking lot name")
                .pricingConfig(pricingConfig).build();

        SpotRent spotRent = SpotRent.builder().id(3).arrivalTime(arrivalTime)
                .leavingTime(leavingTime)
                .vehiclePlate(plate).build();

        Invoice invoice = billingService.billVehicle(mockedLot, price, spotRent);

        verify(pricingPolicyCatalog, times(1)).getPolicyByPolicyType(PolicyType.STANDARD);

        assertNotNull(invoice);
        assertEquals(invoice.getParkingLotId(), mockedLot.getParkingLotId());
        assertEquals(invoice.getParkingName(), mockedLot.getName());
        assertEquals(invoice.getLicensePlate(), plate);
        assertEquals(invoice.getArrivalTime(), arrivalTime);
        assertEquals(invoice.getLeavingTime(), leavingTime);
        assertEquals(invoice.getCost(), price * rentHours, 0);
    }

    @Test (expected = WrongSpotRentException.class)
    public void testBillVehicleWrongRentTiming() throws Exception {
        String plate = "plate";
        float price = 4;
        int rentHours = 2;
        LocalDateTime leavingTime = LocalDateTime.now();
        LocalDateTime arrivalTime = LocalDateTime.now().plusHours(rentHours);

        SpotRent spotRent = SpotRent.builder().id(3).arrivalTime(arrivalTime)
                .leavingTime(leavingTime)
                .vehiclePlate(plate).build();

        billingService.billVehicle(new ParkingLot(), price, spotRent);
    }

    @Test (expected = WrongSpotRentException.class)
    public void testBillVehicleWrongNoRentArrivalTime() throws Exception {
        String plate = "plate";
        float price = 4;
        LocalDateTime leavingTime = LocalDateTime.now();

        SpotRent spotRent = SpotRent.builder().id(3)
                .leavingTime(leavingTime)
                .vehiclePlate(plate).build();

        billingService.billVehicle(new ParkingLot(), price, spotRent);
    }

    @Test (expected = WrongSpotRentException.class)
    public void testBillVehicleNoVehicleInRent() throws Exception {
        float price = 4;
        int rentHours = 2;
        LocalDateTime leavingTime = LocalDateTime.now();
        LocalDateTime arrivalTime = LocalDateTime.now().plusHours(rentHours);

        SpotRent spotRent = SpotRent.builder().id(3).arrivalTime(arrivalTime)
                .leavingTime(leavingTime)
                .build();

        billingService.billVehicle(new ParkingLot(), price, spotRent);
    }
}
