
package org.myproject.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class AddParkingException extends RuntimeException {

    public AddParkingException(String message) {
        super(message);
    }

    public AddParkingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddParkingException(Throwable cause) {
        super(cause);
    }

}
