package im.fitdiary.server.exercise.service.dto;

import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ExerciseLogDetailSlice {

    private final List<ExerciseLogDetail> content;

    private final boolean hasNext;

    public ExerciseLogDetailSlice(List<ExerciseLogDetail> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
