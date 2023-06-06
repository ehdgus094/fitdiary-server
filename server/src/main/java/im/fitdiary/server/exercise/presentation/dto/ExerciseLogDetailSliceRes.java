package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.exercise.service.dto.ExerciseLogDetailSlice;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class ExerciseLogDetailSliceRes {

    private final List<ExerciseLogDetailRes> content;

    private final boolean hasNext;

    public ExerciseLogDetailSliceRes(ExerciseLogDetailSlice exerciseLogDetailSlice) {
        this.content = exerciseLogDetailSlice
                .getContent()
                .stream()
                .map(ExerciseLogDetailRes::new)
                .collect(Collectors.toList());
        this.hasNext = exerciseLogDetailSlice.isHasNext();
    }
}
