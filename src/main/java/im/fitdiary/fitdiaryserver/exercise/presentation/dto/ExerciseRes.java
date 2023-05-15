package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import lombok.Getter;

@Getter
public class ExerciseRes {

    private final Long id;

    private final String name;

    private final ExerciseCategory category;

    private final boolean active;

    public ExerciseRes(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.category = exercise.getCategory();
        this.active = exercise.isActive();
    }
}
