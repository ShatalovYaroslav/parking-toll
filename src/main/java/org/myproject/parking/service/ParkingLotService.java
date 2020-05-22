package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.exception.ResourceNotFoundException;
import org.myproject.parking.model.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class ParkingLotService {

    @Autowired
    private ParkingLotCreator parkingLotCreator;

    private Map<Integer, ParkingLot> parkingMap = new HashMap<>();

    public ParkingLot createParkingLot(ParkingLotMetadata parkingLotMetadata) {
        ParkingLot parkingLot = parkingLotCreator.createParking(parkingLotMetadata);
        parkingMap.put(parkingLot.getParkingLotId(), parkingLot);
        return parkingLot;
    }

    public ParkingLot getParkingLot(Integer parkingId) {
        ParkingLot parkingLot = Optional.ofNullable(parkingMap.get(parkingId))
                .orElseThrow(() -> new ResourceNotFoundException("No parking found for id:" + parkingId));
        return parkingLot;
    }
    public List<ParkingLot> getAllParkingLots() {
        return new ArrayList<ParkingLot>(parkingMap.values());
    }

    public void removeParking(Integer parkingId) {
        getParkingLot(parkingId);
        parkingMap.remove(parkingId);
    }
}
