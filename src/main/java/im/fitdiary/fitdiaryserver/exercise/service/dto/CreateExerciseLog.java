package im.fitdiary.fitdiaryserver.exercise.service.dto;

import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
public class CreateExerciseLog {

    private final Long userId;

    private final int duration;

    @Nullable
    private final LocalDateTime measuredAt;

    public CreateExerciseLog(Long userId, int duration, @Nullable LocalDateTime measuredAt) {
        this.userId = userId;
        this.duration = duration;
        this.measuredAt = measuredAt;
    }

    public ExerciseLog toEntity() {
        return ExerciseLog.create(
                userId,
                duration,
                measuredAt == null ? LocalDateTime.now() : measuredAt
        );
    }
}
