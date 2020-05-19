package org.myproject.parking.model;

import lombok.*;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;

import java.time.LocalDateTime;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpotRent {

    private Vehicle vehicle;
    private LocalDateTime arrivalTime;
    private LocalDateTime leavingTime;
}
