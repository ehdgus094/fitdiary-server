package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.common.converter.TimeConverter;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExerciseLogRes {

    private final Long id;

    private final int duration;

    private final Long measuredAt;

    public ExerciseLogRes(ExerciseLog exerciseLog) {
        this.id = exerciseLog.getId();
        this.duration = exerciseLog.getDuration();
        this.measuredAt = TimeConverter.toTimestamp(exerciseLog.getMeasuredAt());
    }
}
