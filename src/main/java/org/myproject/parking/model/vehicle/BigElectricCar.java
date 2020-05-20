package org.myproject.parking.model.vehicle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BigElectricCar extends Vehicle {
    public BigElectricCar() {
        type = VehicleType.FIFTY_KW;
    }

    public BigElectricCar(String plate) {
        type = VehicleType.FIFTY_KW;
        this.licensePlate = plate;
    }
}
