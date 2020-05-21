package org.myproject.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class BadPolicyParametersException extends ClientException {

    public BadPolicyParametersException(String message) {
        super(message);
    }

    public BadPolicyParametersException(Throwable cause) {
        super(cause);
    }

    public BadPolicyParametersException(String message, Throwable cause) {
        super(message, cause);
    }

}
