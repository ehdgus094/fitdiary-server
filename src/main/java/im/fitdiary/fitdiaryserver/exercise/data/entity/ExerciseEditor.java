package im.fitdiary.fitdiaryserver.exercise.data.entity;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class ExerciseEditor {

    private final JsonNullable<String> name;

    private final JsonNullable<ExerciseCategory> category;

    private final JsonNullable<Boolean> active;

    public ExerciseEditor(
            JsonNullable<String> name,
            JsonNullable<ExerciseCategory> category,
            JsonNullable<Boolean> active
    ) {
        this.name = name;
        this.category = category;
        this.active = active;
    }

    public void edit(Exercise exercise) {
        if (name.isPresent()) exercise.setName(name.get());
        if (category.isPresent()) exercise.setCategory(category.get());
        if (active.isPresent()) exercise.setActive(active.get());
    }
}
