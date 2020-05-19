package org.myproject.parking.model;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "parkingId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

//dto class to send to client for payment
public class Invoice {
    String invoiceId;
    LocalDateTime arrivalTime;
    LocalDateTime leavingTime;

    float cost;
    String licensePlate;
}
