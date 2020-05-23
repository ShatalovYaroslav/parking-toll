package org.myproject.parking.service;

import org.myproject.parking.exception.SpotNotFoundException;
import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.model.ParkingSpot;
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
        List<ParkingSpot> parkingSpotsByType = parkingSpotRepository.
                findParkingSpotsByType(parkingLot.getParkingLotId(), type);

        Optional<ParkingSpot> requiredSpot = parkingSpotsByType.stream()
                .filter(ParkingSpot::isFree).findFirst();

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException(type));
    }

    public ParkingSpot getSpotInParkingByVehiclePlate(ParkingLot parkingLot, String plate) {
        Optional<ParkingSpot> requiredSpot = Optional.ofNullable(
                parkingSpotRepository.findParkingSpotByVehiclePLate(parkingLot.getParkingLotId(), plate));

        return requiredSpot.orElseThrow(() -> new SpotNotFoundException("No parking spot found for vehicle with license plate: " + plate));
    }


}
