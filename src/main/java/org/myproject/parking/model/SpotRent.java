package org.myproject.parking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.myproject.parking.model.vehicle.Vehicle;

import javax.persistence.*;
import java.time.LocalDateTime;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity(name = "SPOTRENT")
@Table(name = "SPOTRENT")
public class SpotRent {

    @Transient
    private Vehicle vehicle;

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private ParkingSpot spot;

    @Column(name = "ARRIVAL_TIME", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    @Column(name = "LEAVING_TIME", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime leavingTime;
}
