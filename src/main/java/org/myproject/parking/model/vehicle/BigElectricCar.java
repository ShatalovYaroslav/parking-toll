package org.myproject.parking.model.vehicle;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonDeserialize(using = JsonDeserializer.None.class)
public class BigElectricCar extends Vehicle {
    public BigElectricCar() {
        type = VehicleType.FIFTY_KW;
    }

    public BigElectricCar(String plate) {
        type = VehicleType.FIFTY_KW;
        this.licensePlate = plate;
    }
}
