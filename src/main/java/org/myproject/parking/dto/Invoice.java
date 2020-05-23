package org.myproject.parking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.myproject.parking.model.SpotRent;

import java.time.LocalDateTime;
import java.time.ZoneId;

@EqualsAndHashCode(of = "invoiceId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

//DTO class to send to client for payment
public class Invoice {
    String invoiceId;
    Integer parkingLotId;
    String parkingName;
    String licensePlate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    LocalDateTime arrivalTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    LocalDateTime leavingTime;

    float cost;

    public Invoice(Integer parkingLotId, String parkingName, SpotRent spotRent, float cost) {
        this.parkingLotId = parkingLotId;
        this.parkingName = parkingName;
        licensePlate = spotRent.getVehiclePlate();
        arrivalTime = spotRent.getArrivalTime();
        leavingTime = spotRent.getLeavingTime();
        this.cost = cost;
        invoiceId = generateInvoiceId();
    }

    //should generate the Invoice unique identifier for parking lot and license plate
    protected String generateInvoiceId(){
        return parkingLotId + "-" + licensePlate + "-"  + leavingTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
