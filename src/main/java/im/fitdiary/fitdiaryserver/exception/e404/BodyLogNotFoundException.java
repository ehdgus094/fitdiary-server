package im.fitdiary.fitdiaryserver.exception.e404;

public class BodyLogNotFoundException extends NotFoundException {
    public BodyLogNotFoundException() {
        super("bodyLog not found");
    }
}
