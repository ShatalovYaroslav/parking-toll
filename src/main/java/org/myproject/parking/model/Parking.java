
package org.myproject.parking.model;

import lombok.*;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;

import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(of = "parkingId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Parking {
    private String parkingId;
    private List<ParkingSpot> spots;
    private Long location; //help find parking

    private PricingPolicy pricingPolicy; //depends on the certain implementation
}
