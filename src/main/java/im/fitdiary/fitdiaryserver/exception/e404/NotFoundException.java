package im.fitdiary.fitdiaryserver.exception.e404;

public abstract class NotFoundException extends RuntimeException {
    protected NotFoundException(String message) {
        super(message);
    }
}
