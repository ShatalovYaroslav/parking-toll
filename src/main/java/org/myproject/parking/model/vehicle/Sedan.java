package org.myproject.parking.model.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sedan extends Vehicle {
    public Sedan() {
        type = VehicleType.GASOLINE;
    }

    public Sedan(String plate) {
        type = VehicleType.GASOLINE;
        this.licensePlate = plate;
    }
}
