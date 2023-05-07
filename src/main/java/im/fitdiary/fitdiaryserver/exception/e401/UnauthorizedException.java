package im.fitdiary.fitdiaryserver.exception.e401;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("unauthorized");
    }
}
