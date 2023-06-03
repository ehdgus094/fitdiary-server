package im.fitdiary.server.exercise.data.dto;

import im.fitdiary.server.exercise.data.entity.ExerciseCategory;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@ToString
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
}
