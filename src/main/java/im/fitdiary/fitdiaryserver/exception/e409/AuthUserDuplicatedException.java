package im.fitdiary.fitdiaryserver.exception.e409;

public class AuthUserDuplicatedException extends ConflictException {

    public AuthUserDuplicatedException() {
        super("auth user already exists");
    }
}
