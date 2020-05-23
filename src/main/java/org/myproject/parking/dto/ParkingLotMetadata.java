package org.myproject.parking.dto;

import lombok.*;
import org.myproject.parking.model.vehicle.VehicleType;

import java.io.Serializable;
import java.util.Map;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingLotMetadata implements Serializable {
    private String name;        //help find and identify parking

    private String policyType;
    private Map<String, String> pricingParameters;

    private Map<VehicleType, Float> priceByVehicleType; //different price for vehicle types

    private Map<VehicleType, Integer> spotsNumberByType;
}
