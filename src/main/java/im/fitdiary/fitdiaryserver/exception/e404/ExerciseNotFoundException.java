package im.fitdiary.fitdiaryserver.exception.e404;

public class ExerciseNotFoundException extends NotFoundException {

    public ExerciseNotFoundException() {
        super("exercise not found");
    }
}
