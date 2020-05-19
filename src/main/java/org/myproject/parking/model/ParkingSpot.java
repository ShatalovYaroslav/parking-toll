
package org.myproject.parking.model;

import lombok.*;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;

import java.time.LocalDateTime;


@EqualsAndHashCode(of = "spotId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingSpot {
    private String spotId; //spot Number, can be alphanumeric value (A12)
    private VehicleType vehicleType;
    private float price;

    //can be in separated class SpotRent, also can save list of transactions
    private Vehicle vehicle;
    private LocalDateTime arrivalTime;
    private LocalDateTime leavingTime;

    public boolean isFree() {
        return vehicle == null;
    }

    public void setupLeavingTime() {
        leavingTime = LocalDateTime.now();
    }

    public void freeSpot() {
        vehicle = null;
        arrivalTime = null;
        leavingTime = null;
    }

    public boolean hasCorrectType(Vehicle vehicle) {
        return vehicle.getType() == vehicleType;
    }

    public boolean placeVehicle(Vehicle veh) {
        if (!hasCorrectType(veh) || !isFree()) {
            return false;
        }
        vehicle = veh;
        arrivalTime = LocalDateTime.now();
        return true;
    }
}
