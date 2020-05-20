
package org.myproject.parking.exception;


import org.myproject.parking.model.vehicle.VehicleType;

public class SpotNotFoundException extends ResourceNotFoundException {

    public SpotNotFoundException() {
        super("No such parking spot");
    }

    public SpotNotFoundException(VehicleType type) {
        super("No free spots for vehicle type: " + type);
    }

    public SpotNotFoundException(String message) {
        super(message);
    }
}
