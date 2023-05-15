package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import lombok.Getter;

@Getter
public class ExerciseLogRes {

    private final Long id;
    private final int duration;

    public ExerciseLogRes(ExerciseLog exerciseLog) {
        this.id = exerciseLog.getId();
        this.duration = exerciseLog.getDuration();
    }
}
