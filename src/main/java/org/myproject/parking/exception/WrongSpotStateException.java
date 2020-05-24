
package org.myproject.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class WrongSpotStateException extends ClientException {

    public WrongSpotStateException(int spotId) {
        super("Forbidden to park in occupied parking spot: " + spotId);
    }

    public WrongSpotStateException(String message) {
        super(message);
    }

    public WrongSpotStateException(Throwable cause) {
        super(cause);
    }

}
