package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.common.validation.annotation.Enum;
import im.fitdiary.server.exercise.data.entity.ExerciseCategory;
import im.fitdiary.server.exercise.data.dto.ExerciseEditor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@Schema
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
