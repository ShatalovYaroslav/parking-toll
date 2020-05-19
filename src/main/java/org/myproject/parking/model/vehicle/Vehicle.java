package org.myproject.parking.model.vehicle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "licensePlate")
@Getter
@Setter
@ToString
public abstract class Vehicle {
    protected String licensePlate;
    protected VehicleType type;
}
