package im.fitdiary.fitdiaryserver.exception.e409;

public abstract class ConflictException extends RuntimeException {

    protected ConflictException(String message) {
        super(message);
    }
}
