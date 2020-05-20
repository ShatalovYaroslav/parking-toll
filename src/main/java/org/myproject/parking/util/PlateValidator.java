package org.myproject.parking.util;

import org.myproject.parking.exception.UnprocessableEntityException;
import org.springframework.stereotype.Component;

@Component
public class PlateValidator {
    public void validateLicensePlate(String licensePlate){
        if(licensePlate.isEmpty() || licensePlate.trim().isEmpty()){
            throw new UnprocessableEntityException("The vehicle license plate can not be empty");
        }
    }
}
