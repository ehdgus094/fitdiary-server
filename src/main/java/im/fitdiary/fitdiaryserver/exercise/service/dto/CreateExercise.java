package im.fitdiary.fitdiaryserver.exercise.service.dto;

import im.fitdiary.fitdiaryserver.exercise.data.entity.Exercise;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseCategory;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class CreateExercise {

    private final Long userId;
    private final String name;
    private final ExerciseCategory category;
    @Nullable
    private final Boolean active;

    public CreateExercise(
            Long userId,
            String name,
            ExerciseCategory category,
            @Nullable Boolean active
    ) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.active = active;
    }

    public Exercise toEntity() {
        return Exercise.create(
                userId,
                name,
                category,
                active == null || active
        );
    }
}