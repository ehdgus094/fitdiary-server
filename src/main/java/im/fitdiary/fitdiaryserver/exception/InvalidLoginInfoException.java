package im.fitdiary.fitdiaryserver.exception;

public class InvalidLoginInfoException extends RuntimeException{
    public InvalidLoginInfoException() {
        super("Invalid Login Info");
    }
}
