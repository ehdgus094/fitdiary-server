package im.fitdiary.fitdiaryserver.exception.e404;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("user not found");
    }
}
