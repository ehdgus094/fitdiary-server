package im.fitdiary.fitdiaryserver.exercise.data.entity;

public enum ExerciseCategory {
    BACK, CHEST, SHOULDER, ARM, ABS, LEG;

    public static ExerciseCategory from(String value) {
        if (value == null) return null;

        for (ExerciseCategory category : ExerciseCategory.values()) {
            if (value.equalsIgnoreCase(category.toString())) {
                return category;
            }
        }
        return null;
    }
}
