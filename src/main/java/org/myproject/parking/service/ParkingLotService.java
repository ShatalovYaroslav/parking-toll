package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.exception.ResourceNotFoundException;
import org.myproject.parking.model.Parking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class ParkingLotService {

    @Autowired
    private ParkingLotCreator parkingLotCreator;

    private Map<Integer, Parking> parkingMap = new HashMap<>();

    public Parking createParking(ParkingLotMetadata parkingLotMetadata) {
        Parking parking = parkingLotCreator.createParking(parkingLotMetadata);
        parkingMap.put(parking.getParkingId(), parking);
        return parking;
    }

    public Parking getParking(Integer parkingId) {
        Parking parking = Optional.ofNullable(parkingMap.get(parkingId))
                .orElseThrow(() -> new ResourceNotFoundException("No parking found for id:" + parkingId));
        return parking;
    }

    public void removeParking(Integer parkingId) {
        getParking(parkingId);
        parkingMap.remove(parkingId);
    }
}
