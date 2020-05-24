
package org.myproject.parking.repository;

import org.myproject.parking.model.persistence.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {

    ParkingLot findOneByParkingLotId(Integer parkingLotId);
}
