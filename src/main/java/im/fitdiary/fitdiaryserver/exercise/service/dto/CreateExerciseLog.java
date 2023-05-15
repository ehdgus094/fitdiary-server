package im.fitdiary.fitdiaryserver.exercise.service.dto;

import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import lombok.Getter;

@Getter
public class CreateExerciseLog {

    private final Long userId;

    private final int duration;

    public CreateExerciseLog(Long userId, int duration) {
        this.userId = userId;
        this.duration = duration;
    }

    public ExerciseLog toEntity() {
        return ExerciseLog.create(
                userId,
                duration
        );
    }
}
