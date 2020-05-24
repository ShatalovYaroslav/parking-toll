package org.myproject.parking.service;

import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.persistence.ParkingLot;
import org.myproject.parking.model.persistence.ParkingSpot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.myproject.parking.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("spotService")
@Transactional
public class ParkingSpotService {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    public ParkingSpot findSpotById(ParkingLot parkingLot, int spotId) {
        Optional<ParkingSpot> requiredSpot = Optional.ofNullable(
                parkingSpotRepository.findParkingSpotInLot(parkingLot.getParkingLotId(), spotId));

        return requiredSpot.orElseThrow(SpotNotFoundException::new);
    }

    public ParkingSpot getFreeSpotInParkingByType(ParkingLot parkingLot, VehicleType type) {
        List<ParkingSpot> freeSpotsForType  = parkingSpotRepository.
                findFreeParkingSpotsByType(parkingLot.getParkingLotId(), type);
        if(freeSpotsForType == null){
            throw new SpotNotFoundException(type);
        }
        return freeSpotsForType.get(0);
    }

    public ParkingSpot getSpotInParkingByVehiclePlate(ParkingLot parkingLot, String plate) {
        Optional<ParkingSpot> requiredSpot = Optional.ofNullable(
                parkingSpotRepository.findParkingSpotByVehiclePLate(parkingLot.getParkingLotId(), plate));

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException("No parking spot found for vehicle with license plate: " + plate));
    }


}
