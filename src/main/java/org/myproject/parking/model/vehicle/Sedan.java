package org.myproject.parking.model.vehicle;

import lombok.*;

@Getter
@Setter
@ToString
public class Sedan extends Vehicle {
    public Sedan(){
        type = VehicleType.Gasoline;
    }

    public Sedan(String plate){
        type = VehicleType.Gasoline;
        this.licensePlate = plate;
    }
}
