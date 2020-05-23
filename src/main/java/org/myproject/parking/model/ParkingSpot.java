package org.myproject.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;

import javax.persistence.*;
import java.time.LocalDateTime;


@EqualsAndHashCode(of = "spotId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"parkingLot"})
@Data
@Entity(name = "PARKINGSPOT")
@Table(name = "PARKINGSPOT", uniqueConstraints = @UniqueConstraint(columnNames = { "LOT_ID", "SPOT_ID"}))
public class ParkingSpot {
    @Id
    @Column(name = "SPOT_ID")
    private Integer spotId; // Parking Spot ID is unique per Parking Lot, it belongs to

    @OneToOne(orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    @JoinColumn(name="ID")
    private SpotRent spotRent; //corresponds to last rent operation, but as improvement can save a list of spot Rental transactions

    @Column(name = "VEHICLE_TYPE")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "PRICE")
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_ID", referencedColumnName = "LOT_ID")
    @JsonIgnore
    private ParkingLot parkingLot;

    public ParkingSpot(Integer spotId, VehicleType vehicleType, float price) {
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
        spotRent.setVehiclePlate(veh.getLicensePlate());
        spotRent.setArrivalTime(LocalDateTime.now());
        return true;
    }
}
