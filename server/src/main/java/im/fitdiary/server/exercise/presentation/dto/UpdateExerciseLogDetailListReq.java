package im.fitdiary.server.exercise.presentation.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@ToString
public class UpdateExerciseLogDetailListReq {

    @NotNull
    @Positive
    private Long exerciseLogId;

    @NotNull
    @Size(min = 1, max = 100)
    @Valid
    private List<UpdateExerciseLogDetailReq> data;
}
