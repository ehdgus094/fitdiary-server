package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class ExerciseLogDetailRes {

    private final Long id;

    private final int sequence;

    private final boolean warmUp;

    private final BigDecimal weight;

    private final int count;

    private final int supportCount;

    public ExerciseLogDetailRes(ExerciseLogDetail detail) {
        this.id = detail.getId();
        this.sequence = detail.getSequence();
        this.warmUp = detail.isWarmUp();
        this.weight = detail.getWeight();
        this.count = detail.getCount();
        this.supportCount = detail.getSupportCount();
    }
}
