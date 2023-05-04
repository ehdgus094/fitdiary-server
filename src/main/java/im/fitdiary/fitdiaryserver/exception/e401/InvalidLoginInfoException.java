package im.fitdiary.fitdiaryserver.exception.e401;

public class InvalidLoginInfoException extends RuntimeException {
    public InvalidLoginInfoException() {
        super("Invalid Login Info");
    }
}
