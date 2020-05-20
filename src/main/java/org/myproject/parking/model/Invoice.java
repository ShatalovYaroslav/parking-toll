package org.myproject.parking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "invoiceId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

//DTO class to send to client for payment
public class Invoice {
    Integer invoiceId;
    String parkingName;
    String licensePlate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    LocalDateTime arrivalTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    LocalDateTime leavingTime;

    float cost;

    public Invoice(String parkingName, SpotRent spotRent, float cost) {
        this.parkingName = parkingName;
        licensePlate = spotRent.getVehicle().getLicensePlate();
        arrivalTime = spotRent.getArrivalTime();
        leavingTime = spotRent.getLeavingTime();
        this.cost = cost;
    }
}
