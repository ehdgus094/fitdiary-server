package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@ToString
public class CreateExerciseLogDetailListReq {

    @NotNull
    @Positive
    private Long exerciseLogId;

    @NotNull
    @Size(min = 1, max = 100)
    @Valid
    private List<CreateExerciseLogDetailReq> data;
}
