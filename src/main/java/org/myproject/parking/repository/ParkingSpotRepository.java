
package org.myproject.parking.repository;

import org.myproject.parking.model.ParkingLot;
import org.myproject.parking.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {

    ParkingLot findOneBySpotId(Integer spotId);
}
