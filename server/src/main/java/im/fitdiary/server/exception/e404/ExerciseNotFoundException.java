package im.fitdiary.server.exception.e404;

public class ExerciseNotFoundException extends NotFoundException {

    public ExerciseNotFoundException() {
        super("exercise not found");
    }
}
