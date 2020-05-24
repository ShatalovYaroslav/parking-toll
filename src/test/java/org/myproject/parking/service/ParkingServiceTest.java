
package org.myproject.parking.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.exception.WrongSpotStateException;
import org.myproject.parking.model.persistence.ParkingLot;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.persistence.SpotRent;
import org.myproject.parking.model.vehicle.BigElectricCar;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.util.PlateValidator;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ParkingServiceTest {

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private ParkingSpotService parkingSpotService;

    @Mock
    private BillingService billingService;

    @Mock
    private ParkingLotService parkingLotService;

    @Mock
    private PlateValidator plateValidator;

    @Test
    public void testParkVehicle() throws Exception {
        int parkingId = 1;
        String plate = "plate";
        Vehicle vehicle = new BigElectricCar("plate");
        VehicleType type = vehicle.getType();

        ParkingLot mockedLot = new ParkingLot();
        when(parkingLotService.getParkingLotAndCheck(anyInt()))
                .thenReturn(mockedLot);

        ParkingSpot mockedSpot = new ParkingSpot(1, null, type, 3.6f,  null);
        when(parkingSpotService.getFreeSpotInParkingByType(mockedLot, type))
                .thenReturn(mockedSpot);

        ParkingSpot parkedSpot = parkingService.parkVehicle(parkingId, vehicle);

        InOrder inOrder = Mockito.inOrder(plateValidator,
                parkingLotService,
                parkingSpotService,
                parkingLotService);

        inOrder.verify(plateValidator, times(1)).validateLicensePlate(plate);
        inOrder.verify(parkingLotService, times(1)).getParkingLotAndCheck(parkingId);
        inOrder.verify(parkingSpotService, times(1)).getFreeSpotInParkingByType(mockedLot, type);
        inOrder.verify(parkingLotService, times(1)).updateParkingLot(mockedLot);

        assertFalse(parkedSpot.isFree());
        assertEquals(parkedSpot.getSpotId(), mockedSpot.getSpotId());
        assertEquals(parkedSpot.getVehicleType(), type);
        assertNotNull(parkedSpot.getSpotRent().getArrivalTime());
        assertNull(parkedSpot.getSpotRent().getLeavingTime());
        assertEquals(parkedSpot.getSpotRent().getVehiclePlate(), plate);
    }

    @Test(expected = WrongSpotStateException.class)
    public void testParkVehicleThrowExceptionForBusySpot() throws Exception {
        ParkingSpot mockedSpot = new ParkingSpot(1, new SpotRent(), VehicleType.GASOLINE, 3.6f,  null);
        when(parkingSpotService.getFreeSpotInParkingByType(any(ParkingLot.class), any(VehicleType.class)))
                .thenReturn(mockedSpot);

        parkingService.parkVehicle(1, new Sedan());
    }

    @Test
    public void testLeavePark() throws Exception {
        int parkingId = 1;
        String plate = "plate";
        Vehicle vehicle = new BigElectricCar("plate");
        VehicleType type = vehicle.getType();

        ParkingLot mockedLot = new ParkingLot();
        when(parkingLotService.getParkingLotAndCheck(anyInt()))
                .thenReturn(mockedLot);

        SpotRent spotRent = SpotRent.builder().id(3).arrivalTime(LocalDateTime.now()).vehiclePlate(plate).build();
        ParkingSpot mockedSpot = new ParkingSpot(1, spotRent, type, 3.6f,  null);
        when(parkingSpotService.getSpotInParkingByVehiclePlate(mockedLot, plate))
                .thenReturn(mockedSpot);

        Invoice mockedInvoice = new Invoice();
        when(billingService.billVehicle(mockedLot, mockedSpot.getPrice(), spotRent))
                .thenReturn(mockedInvoice);

        Invoice invoice = parkingService.leaveParking(parkingId, plate);

        InOrder inOrder = Mockito.inOrder(
                parkingLotService,
                parkingSpotService,
                billingService,
                parkingLotService);

        inOrder.verify(parkingLotService, times(1)).getParkingLotAndCheck(parkingId);
        inOrder.verify(parkingSpotService, times(1)).getSpotInParkingByVehiclePlate(mockedLot, plate);
        inOrder.verify(billingService, times(1)).
                billVehicle(mockedLot, mockedSpot.getPrice(), spotRent);
        inOrder.verify(parkingLotService, times(1)).updateParkingLot(mockedLot);

        assertEquals(invoice, mockedInvoice);
    }

    @Test
    public void testGetParkingSpotById(){
        Integer parkingLotId = 1;
        Integer spotId = 1;
        ParkingLot parkingLot = new ParkingLot();
        when(parkingLotService.getParkingLotAndCheck(parkingLotId))
                .thenReturn(parkingLot);
        parkingService.getParkingSpotById(parkingLotId, spotId);

        verify(parkingLotService, times(1)).getParkingLotAndCheck(parkingLotId);
        verify(parkingSpotService, times(1)).findSpotById(parkingLot, spotId);
    }

    @Test(expected = WrongSpotStateException.class)
    public void testLeaveParkThrowExceptionForSpotWithoutVehicle() throws Exception {
        ParkingSpot mockedSpot = new ParkingSpot(1, null, VehicleType.GASOLINE, 3.6f,  null);
        when(parkingSpotService.getSpotInParkingByVehiclePlate(any(ParkingLot.class), anyString()))
                .thenReturn(mockedSpot);

        parkingService.leaveParking(1, "some plate");
    }
}
