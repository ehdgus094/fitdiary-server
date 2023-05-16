package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.common.validation.Enum;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExercise;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CreateExerciseReq {

    @NotBlank(message = "name should not be empty")
    private String name;

    @Enum(enumClass = ExerciseCategory.class, message = "incorrect category format")
    @NotNull(message = "category should not be null")
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
