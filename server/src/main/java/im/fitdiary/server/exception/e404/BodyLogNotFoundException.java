package im.fitdiary.server.exception.e404;

public class BodyLogNotFoundException extends NotFoundException {

    public BodyLogNotFoundException() {
        super("body log not found");
    }
}
