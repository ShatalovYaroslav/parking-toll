
package org.myproject.parking.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.myproject.parking.model.Invoice;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class ParkingControllerRestTest {

    @InjectMocks
    private ParkingController parkingController;

    @Mock
    private ParkingService parkingService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParkVehicle() {
        Integer parkId = 1;
        Vehicle vehicle = new Sedan();

        ResponseEntity<ParkingSpot> response = parkingController.parkVehicle(parkId, vehicle);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        verify(parkingService, times(1)).parkVehicle(parkId, vehicle);
    }

    @Test
    public void testLeaveParking() {
        Integer parkId = 1;
        String plate = "plate";

        ResponseEntity<Invoice> response = parkingController.leaveParking(parkId, plate);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        verify(parkingService, times(1)).leaveParking(parkId, plate);
    }

}
