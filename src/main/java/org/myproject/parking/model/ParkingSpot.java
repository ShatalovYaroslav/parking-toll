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
    //corresponds to last rent operation, but as improvement can save a list of spot Rental transactions
    private SpotRent spotRent;
    private int spotId;
    private VehicleType vehicleType;
    private float price;

    public ParkingSpot(int spotId, VehicleType vehicleType, float price) {
        this.spotId = spotId;
        this.vehicleType = vehicleType;
        this.price = price;
    }

    public boolean isFree() {
        return spotRent == null;
    }

    public void setupLeavingTime() {
        spotRent.setLeavingTime(LocalDateTime.now());
    }

    public void freeSpot() {
        spotRent = null;
    }

    public boolean hasCorrectType(Vehicle vehicle) {
        return vehicle.getType() == vehicleType;
    }

    public boolean placeVehicle(Vehicle veh) {
        if (!hasCorrectType(veh) || !isFree()) {
            return false;
        }
        spotRent = new SpotRent();
        spotRent.setVehicle(veh);
        spotRent.setArrivalTime(LocalDateTime.now());
        return true;
    }
}
