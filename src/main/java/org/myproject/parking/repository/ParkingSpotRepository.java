
package org.myproject.parking.repository;

import org.myproject.parking.model.ParkingSpot;
import org.myproject.parking.model.vehicle.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer>,
        JpaSpecificationExecutor<ParkingSpot> {

    ParkingSpot findOneBySpotId(Integer spotId);

    @Query("SELECT spot FROM PARKINGSPOT spot WHERE spot.parkingLot.parkingLotId = ?1 AND spot.spotId = ?2")
    ParkingSpot findParkingSpotInLot(Integer parkingLotId, Integer spotId);

    @Query("SELECT spot FROM PARKINGSPOT spot WHERE spot.spotRent is null AND " +
            "spot.parkingLot.parkingLotId = :lotId AND spot.vehicleType = :type")
    List<ParkingSpot> findFreeParkingSpotsByType(@Param("lotId")Integer parkingLotId, @Param("type") VehicleType type);

    @Query("SELECT spot FROM PARKINGSPOT spot WHERE spot.parkingLot.parkingLotId = ?1 AND spot.spotRent.vehiclePlate = ?2")
    ParkingSpot findParkingSpotByVehiclePLate(Integer parkingLotId, String plate);
}
