package org.myproject.parking.model.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmallElectricCar extends Vehicle {
    public SmallElectricCar() {
        type = VehicleType.GASOLINE;
    }

    public SmallElectricCar(String plate) {
        type = VehicleType.TWENTY_KW;
        this.licensePlate = plate;
    }
}
