package im.fitdiary.fitdiaryserver.exercise.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ExerciseCategory {
    BACK, CHEST, SHOULDER, ARM, ABS, LEG;

    @JsonCreator
    public static ExerciseCategory from(String value) {
        for (ExerciseCategory category : ExerciseCategory.values()) {
            if (value.equalsIgnoreCase(category.toString())) {
                return category;
            }
        }
        return null;
    }
}
