package im.fitdiary.server.exception.e401;

public class InvalidLoginInfoException extends BaseUnauthorizedException {

    public InvalidLoginInfoException() {
        super("invalid login info");
    }
}
