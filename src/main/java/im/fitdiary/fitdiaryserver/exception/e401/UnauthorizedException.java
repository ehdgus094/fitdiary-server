package im.fitdiary.fitdiaryserver.exception.e401;

public class UnauthorizedException extends BaseUnauthorizedException {
    public UnauthorizedException() {
        super("unauthorized");
    }
}
