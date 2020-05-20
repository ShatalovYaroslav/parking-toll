package org.myproject.parking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.myproject.parking.model.vehicle.Vehicle;

import java.time.LocalDateTime;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpotRent {

    private Vehicle vehicle;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime leavingTime;
}
