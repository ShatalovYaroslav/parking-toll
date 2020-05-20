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
public class SmallElectricCar extends Vehicle {
    public SmallElectricCar() {
        type = VehicleType.TWENTY_KW;
    }

    public SmallElectricCar(String plate) {
        type = VehicleType.TWENTY_KW;
        this.licensePlate = plate;
    }
}
