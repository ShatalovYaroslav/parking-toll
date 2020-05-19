package org.myproject.parking.model.vehicle;

import lombok.*;

@EqualsAndHashCode(of = "licensePlate")
@Getter
@Setter
@ToString
public abstract class Vehicle {
    protected String licensePlate;
    protected VehicleType type;
}
