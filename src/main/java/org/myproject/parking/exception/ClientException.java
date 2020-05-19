
package org.myproject.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException extends RuntimeException {

    public ClientException() {
        super();
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

}
