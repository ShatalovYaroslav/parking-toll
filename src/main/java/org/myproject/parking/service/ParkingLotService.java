package org.myproject.parking.service;

import lombok.extern.log4j.Log4j2;
import org.myproject.parking.dto.ParkingLotMetadata;
import org.myproject.parking.exception.ResourceNotFoundException;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
@Transactional
public class ParkingLotService {

    @Autowired
    private ParkingLotCreator parkingLotCreator;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    public ParkingLot createParkingLot(ParkingLotMetadata parkingLotMetadata) {
        ParkingLot parkingLot = parkingLotCreator.createParking(parkingLotMetadata);

        ParkingLot parkingLotSaved = parkingLotRepository.save(parkingLot);
        return parkingLotSaved;
    }

    public ParkingLot getParkingLotAndCheck(Integer parkingId) {
        ParkingLot parkingLot = Optional.ofNullable(parkingLotRepository.findOneByParkingLotId(parkingId))
                .orElseThrow(() -> new ResourceNotFoundException("No parking found for id:" + parkingId));
        return parkingLot;
    }

    public ParkingLot updateParkingLot(ParkingLot parkingLot) {
        getParkingLotAndCheck(parkingLot.getParkingLotId());
        parkingLotRepository.save(parkingLot);
        return parkingLot;
    }

    public ParkingLot removeParkingLot(Integer parkingId) {
        ParkingLot parkingLot = getParkingLotAndCheck(parkingId);
        parkingLotRepository.delete(parkingId);
        return parkingLot;
    }

    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAll();
    }
}
