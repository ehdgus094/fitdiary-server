package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseEditor;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateExerciseReq {

    @NotBlank(message = "name should not be empty")
    private JsonNullable<String> name = JsonNullable.undefined();

    @Enum(enumClass = ExerciseCategory.class, message = "incorrect category format")
    @NotNull(message = "category should not be null")
    private JsonNullable<String> category = JsonNullable.undefined();

    @NotNull(message = "active should not be null")
    private JsonNullable<Boolean> active = JsonNullable.undefined();

    public ExerciseEditor toEditor() {
        return new ExerciseEditor(
                name,
                category.isPresent()
                        ? JsonNullable.of(ExerciseCategory.from(category.get()))
                        : JsonNullable.undefined(),
                active
        );
    }
}
