package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;
import im.fitdiary.fitdiaryserver.common.validation.annotation.PastOrPresentTimestamp;
import im.fitdiary.fitdiaryserver.exercise.service.dto.CreateExerciseLog;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@ToString
public class CreateExerciseLogReq {

    @NotNull(message = "duration should not be null")
    @Positive(message = "duration should be positive")
    @Max(value = 86400, message = "duration should not be over than 86400") // 1일
    private Integer duration;

    @PastOrPresentTimestamp(message = "measuredAt must be past or present")
    private Long measuredAt;

    public CreateExerciseLog toDto(Long userId) {
        return new CreateExerciseLog(
                userId,
                duration,
                TimeConverter.toLocalDateTime(measuredAt)
        );
    }
}
