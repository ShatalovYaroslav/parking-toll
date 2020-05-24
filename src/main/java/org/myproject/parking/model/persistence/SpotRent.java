package org.myproject.parking.model.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
@Entity(name = "SPOTRENT")
@Table(name = "SPOTRENT")
public class SpotRent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @JsonIgnore
    private Integer id;

    @Column(name = "VEHICLE_PLATE")
    private String vehiclePlate;

    @Column(name = "ARRIVAL_TIME", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    @Column(name = "LEAVING_TIME", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime leavingTime;
}
