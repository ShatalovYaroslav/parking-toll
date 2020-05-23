
package org.myproject.parking.repository;

import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer>,
        JpaSpecificationExecutor<ParkingSpot> {

    ParkingSpot findOneBySpotId(Integer spotId);

    @Query("SELECT spot FROM PARKINGSPOT spot WHERE spot.parkingLot.parkingLotId = ?1 AND spot.spotId = ?2")
    ParkingSpot findParkingSpotInLot(Integer parkingLotId, Integer spotId);

    @Query("SELECT spot FROM PARKINGSPOT spot WHERE spot.parkingLot.parkingLotId = ?1 AND spot.vehicleType = ?2")
    List<ParkingSpot> findParkingSpotsByType(Integer parkingLotId, VehicleType type);

    @Query("SELECT spot FROM PARKINGSPOT spot WHERE spot.parkingLot.parkingLotId = ?1 AND spot.spotRent.vehiclePlate = ?2")
    ParkingSpot findParkingSpotByVehiclePLate(Integer parkingLotId, String plate);
}
