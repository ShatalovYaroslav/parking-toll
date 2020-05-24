
package org.myproject.parking.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.exception.ResourceNotFoundException;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.repository.ParkingLotRepository;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParkingLotServiceTest {

    @InjectMocks
    private ParkingLotService parkingLotService;

    @Mock
    private ParkingLotCreator parkingLotCreator;

    @Mock
    ParkingLotRepository parkingLotRepository;

    public ParkingLot createParkingLot(ParkingLotMetadata parkingLotMetadata) {
        ParkingLot parkingLot = parkingLotCreator.createParking(parkingLotMetadata);

        ParkingLot parkingLotSaved = parkingLotRepository.save(parkingLot);
        return parkingLotSaved;
    }

    @Test
    public void testCreateParkingLot() {
        ParkingLotMetadata parkingLotMetadata = new ParkingLotMetadata();
        ParkingLot parkingLot = new ParkingLot();
        when(parkingLotCreator.createParking(parkingLotMetadata))
                .thenReturn(parkingLot);

        parkingLotService.createParkingLot(parkingLotMetadata);

        verify(parkingLotCreator, times(1)).createParking(parkingLotMetadata);
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test (expected = ResourceNotFoundException.class)
    public void testGetParkingLotException() {
        Integer parkingId = 2;
        when(parkingLotRepository.findOneByParkingLotId(parkingId))
                .thenReturn(null);

        parkingLotService.getParkingLotAndCheck(parkingId);
    }

    public void testGetParkingLot() {
        Integer parkingId = 2;
        parkingLotService.getParkingLotAndCheck(parkingId);
        verify(parkingLotRepository, times(1)).findOneByParkingLotId(parkingId);
    }

    public void testGetAllParkingLots() {
        parkingLotService.getAllParkingLots();
        verify(parkingLotRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateParkingLot() {
        Integer parkingId = 2;
        ParkingLot parkingLot = ParkingLot.builder().parkingLotId(parkingId).build();
        when(parkingLotRepository.findOneByParkingLotId(parkingId))
                .thenReturn(parkingLot);

        parkingLotService.updateParkingLot(parkingLot);

        verify(parkingLotRepository, times(1)).findOneByParkingLotId(parkingId);
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    public void testRemoveParkingLot() {
        Integer parkingId = 2;
        ParkingLot parkingLot = ParkingLot.builder().parkingLotId(parkingId).build();
        when(parkingLotRepository.findOneByParkingLotId(parkingId))
                .thenReturn(parkingLot);

        parkingLotService.removeParkingLot(parkingId);

        verify(parkingLotRepository, times(1)).findOneByParkingLotId(parkingId);
        verify(parkingLotRepository, times(1)).delete(parkingId);
    }

}
