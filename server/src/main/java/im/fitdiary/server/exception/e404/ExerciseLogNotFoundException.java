package im.fitdiary.server.exception.e404;

public class ExerciseLogNotFoundException extends NotFoundException {

    public ExerciseLogNotFoundException() {
        super("exercise log not found");
    }
}
