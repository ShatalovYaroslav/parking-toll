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
public class Sedan extends Vehicle {
    public Sedan() {
        type = VehicleType.GASOLINE;
    }

    public Sedan(String plate) {
        type = VehicleType.GASOLINE;
        this.licensePlate = plate;
    }
}
