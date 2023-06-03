package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.common.validation.annotation.Enum;
import im.fitdiary.server.exercise.data.entity.ExerciseCategory;
import im.fitdiary.server.exercise.service.dto.CreateExercise;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class CreateExerciseReq {

    @NotBlank
    private String name;

    @Enum(enumClass = ExerciseCategory.class)
    @NotNull
    private String category;

    private Boolean active;

    public CreateExercise toDto(Long userId) {
        return new CreateExercise(
                userId,
                name,
                ExerciseCategory.from(category),
                active
        );
    }
}
