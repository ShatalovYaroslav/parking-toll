package org.myproject.parking.model.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.myproject.parking.exception.UnprocessableEntityException;

@EqualsAndHashCode(of = "licensePlate")
@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = BigElectricCar.class, name ="fifty_kw"),
        @JsonSubTypes.Type(value = SmallElectricCar.class, name = "twenty_kw"),
        @JsonSubTypes.Type(value = Sedan.class, name = "gasoline") })
public abstract class Vehicle {

    protected String licensePlate;
    protected VehicleType type;

}
