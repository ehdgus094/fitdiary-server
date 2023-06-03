package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.data.entity.ExerciseCategory;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
