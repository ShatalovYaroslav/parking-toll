package org.myproject.parking.model.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

public enum VehicleType {
    @JsonProperty("gasoline")
    GASOLINE ("gasoline"),

    @JsonProperty("twenty_kw")
    TWENTY_KW ("twenty_kw"),

    @JsonProperty("fifty_kw")
    FIFTY_KW ("fifty_kw");

    private final String type;

    VehicleType(final String type) {
        this.type = type;
    }


    @JsonCreator
    public static VehicleType convert(String type) {
        if (type == null) {
            return null;
        }
        return VehicleType.valueOf(type.toUpperCase());
    }


    @Override
    public String toString() {
        return type;
    }
}
