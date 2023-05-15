package im.fitdiary.fitdiaryserver.exception.e404;

public class AuthUserNotFoundException extends NotFoundException {

    public AuthUserNotFoundException() {
        super("auth user not found");
    }
}
