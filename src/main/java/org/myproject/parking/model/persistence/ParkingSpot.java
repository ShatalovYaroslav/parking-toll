package org.myproject.parking.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPOT_SEQUENCE")
    @GenericGenerator(name = "SPOT_SEQUENCE", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {@org.hibernate.annotations.Parameter(name = "sequence_name", value = "SPOT_SEQUENCE"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1")})
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

    public ParkingSpot(VehicleType vehicleType, float price) {
        this.vehicleType = vehicleType;
        this.price = price;
    }

    public boolean isFree() {
        return spotRent == null;
    }

    public boolean setupLeavingTime() {
        if (isFree() || spotRent.getArrivalTime() == null) {
            return false;
        }
        spotRent.setLeavingTime(LocalDateTime.now());
        return true;
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
