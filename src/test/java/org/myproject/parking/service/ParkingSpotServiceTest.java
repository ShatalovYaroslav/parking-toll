
package org.myproject.parking.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.persistence.ParkingLot;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.repository.ParkingSpotRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParkingSpotServiceTest {

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    @Mock
    ParkingSpotRepository parkingSpotRepository;

    @Test
    public void testFindSpotById() {
        Integer parkingLotId = 3;
        Integer spotId = 2;
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setParkingLotId(parkingLotId);
        ParkingSpot mockedSpot = new ParkingSpot();
        when(parkingSpotRepository.findParkingSpotInLot(parkingLot.getParkingLotId(), spotId))
                .thenReturn(mockedSpot);

        ParkingSpot spot = parkingSpotService.findSpotById(parkingLot, spotId);

        verify(parkingSpotRepository, times(1))
                .findParkingSpotInLot(parkingLot.getParkingLotId(), spotId);
        assertEquals(spot, mockedSpot);
    }

    @Test
    public void testGetFreeSpotInParkingByType() {
        Integer parkingLotId = 3;
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setParkingLotId(parkingLotId);
        ParkingSpot mockedSpot = new ParkingSpot();
        VehicleType type = VehicleType.FIFTY_KW;
        List<ParkingSpot> freeSpotsForType = new ArrayList<>();
        freeSpotsForType.add(mockedSpot);

        when(parkingSpotRepository.findFreeParkingSpotsByType(parkingLot.getParkingLotId(), type))
                .thenReturn(freeSpotsForType);

        ParkingSpot spot = parkingSpotService.getFreeSpotInParkingByType(parkingLot, type);

        verify(parkingSpotRepository, times(1))
                .findFreeParkingSpotsByType(parkingLot.getParkingLotId(), type);
        assertEquals(spot, mockedSpot);
    }

    @Test (expected = SpotNotFoundException.class)
    public void testGetFreeSpotInParkingByTypeNotFound() {
        Integer parkingLotId = 3;
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setParkingLotId(parkingLotId);
        VehicleType type = VehicleType.FIFTY_KW;
        when(parkingSpotRepository.findFreeParkingSpotsByType(parkingLot.getParkingLotId(), type))
                .thenReturn(null);

        ParkingSpot spot = parkingSpotService.getFreeSpotInParkingByType(parkingLot, type);
    }

    @Test
    public void testGetSpotInParkingByVehiclePlate() {
        Integer parkingLotId = 3;
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setParkingLotId(parkingLotId);
        ParkingSpot mockedSpot = new ParkingSpot();
        String plate = "test";

        when(parkingSpotRepository.findParkingSpotByVehiclePLate(parkingLot.getParkingLotId(), plate))
                .thenReturn(mockedSpot);

        ParkingSpot spot = parkingSpotService.getSpotInParkingByVehiclePlate(parkingLot, plate);

        verify(parkingSpotRepository, times(1))
                .findParkingSpotByVehiclePLate(parkingLot.getParkingLotId(), plate);
        assertEquals(spot, mockedSpot);
    }

    @Test (expected = SpotNotFoundException.class)
    public void testGetSpotInParkingByVehiclePlateNotFound() {
        ParkingLot parkingLot = new ParkingLot();
        String plate = "test";

        when(parkingSpotRepository.findParkingSpotByVehiclePLate(parkingLot.getParkingLotId(), plate))
                .thenReturn(null);

        parkingSpotService.getSpotInParkingByVehiclePlate(parkingLot, plate);
    }
}
