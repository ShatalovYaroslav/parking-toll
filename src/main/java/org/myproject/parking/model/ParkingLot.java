package org.myproject.parking.model;

import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(of = "parkingLotId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity(name = "PARKINGLOT")
@Table(name = "PARKINGLOT")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOT_SEQUENCE")
    @GenericGenerator(name = "LOT_SEQUENCE", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = { @Parameter(name = "sequence_name", value = "LOT_SEQUENCE"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "ID")
    private Integer parkingLotId;    //parkingId is autogenerated by DB

    @Column(name = "NAME")
    private String name;        //help to find and identify parking

    @OneToMany(mappedBy = "parkingLot",
            fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private List<ParkingSpot> spots = new ArrayList<>();

    @OneToOne( cascade = { CascadeType.ALL })
    @JoinColumn(name="ID")
    private PricingConfig pricingConfig; //Pricing Policy depends on the certain implementation

    public ParkingLot(String name, List<ParkingSpot> spots, PricingConfig pricingConfig) {
        this.name = name;
        this.spots = spots;
        this.pricingConfig = pricingConfig;
    }

    public ParkingLot(String name, PricingConfig pricingConfig) {
        this.name = name;
        this.pricingConfig = pricingConfig;
    }

    public void addParkingSpot(ParkingSpot parkingSpot) {
        this.spots.add(parkingSpot);
        parkingSpot.setParkingLot(this);
    }
}
