package im.fitdiary.fitdiaryserver.exception.e401;

public abstract class BaseUnauthorizedException extends RuntimeException {

    protected BaseUnauthorizedException(String message) {
        super(message);
    }
}
