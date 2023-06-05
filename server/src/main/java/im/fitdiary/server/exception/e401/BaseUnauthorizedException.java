package im.fitdiary.server.exception.e401;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public abstract class BaseUnauthorizedException extends RuntimeException {

    protected BaseUnauthorizedException(String message) {
        super(message);
    }
}
