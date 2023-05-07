package im.fitdiary.fitdiaryserver.exception.e409;

public class UserDuplicatedException extends DuplicatedException {
    public UserDuplicatedException() {
        super("user duplicated");
    }
}
