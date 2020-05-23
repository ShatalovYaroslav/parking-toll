package org.myproject.parking.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
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
@ToString
@Data
@Entity(name = "PARKINSPOT")
@Table(name = "PARKINSPOT")
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPOT_SEQUENCE")
    @GenericGenerator(name = "SPOT_SEQUENCE", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {@org.hibernate.annotations.Parameter(name = "sequence_name", value = "SPOT_SEQUENCE"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1")})
    @Column(name = "SPOT_ID")
    private int spotId;

    @OneToOne(mappedBy = "spot", orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    private SpotRent spotRent; //corresponds to last rent operation, but as improvement can save a list of spot Rental transactions

    @Column(name = "VEHICLE_TYPE")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "PRICE")
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ParkingLot parkingLot;

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
