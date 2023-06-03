package im.fitdiary.server.exercise.service.dto;

import im.fitdiary.server.exercise.data.entity.Exercise;
import im.fitdiary.server.exercise.data.entity.ExerciseCategory;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@ToString
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