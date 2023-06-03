package im.fitdiary.server.exercise.service.dto;

import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@ToString
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
