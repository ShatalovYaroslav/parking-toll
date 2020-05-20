package org.myproject.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends ClientException {

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(Throwable cause) {
        super(cause);
    }

}
