
package org.myproject.parking.exception;

import org.myproject.parking.exception.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class WrongSpotRentException extends ClientException {

    public WrongSpotRentException(int spotId) {
        super("Forbidden to park in occupied parking spot: " + spotId);
    }

    public WrongSpotRentException(Throwable cause) {
        super(cause);
    }

    public WrongSpotRentException(String message) {
        super(message);
    }

}
