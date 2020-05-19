package org.myproject.parking.model;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "parkingId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

//DTO class to send to client for payment
public class Invoice {
    Integer invoiceId;
    String licensePlate;
    LocalDateTime arrivalTime;
    LocalDateTime leavingTime;

    float cost;

    public Invoice(SpotRent spotRent, float cost){
        licensePlate = spotRent.getVehicle().getLicensePlate();
        arrivalTime = spotRent.getArrivalTime();
        leavingTime = spotRent.getLeavingTime();
        this.cost = cost;
    }
}
