package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.common.validation.annotation.Enum;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseEditor;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class UpdateExerciseReq {

    @NotBlank
    private JsonNullable<String> name = JsonNullable.undefined();

    @Enum(enumClass = ExerciseCategory.class)
    @NotNull
    private JsonNullable<String> category = JsonNullable.undefined();

    @NotNull
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
