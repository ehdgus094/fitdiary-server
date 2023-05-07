package im.fitdiary.fitdiaryserver.exception.e409;

public abstract class DuplicatedException extends RuntimeException {
    protected DuplicatedException(String message) {
        super(message);
    }
}
