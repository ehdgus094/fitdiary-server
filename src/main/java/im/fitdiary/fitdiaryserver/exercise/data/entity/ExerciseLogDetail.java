package im.fitdiary.fitdiaryserver.exercise.data.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"exercise", "exerciseLog"})
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE exercise_log_detail SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class ExerciseLogDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExerciseLog exerciseLog;

    @Getter(AccessLevel.NONE)
    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private int sequence; // 운동 순서

    @Column(nullable = false)
    private boolean warmUp;

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private int intervals; // seconds

    @Column(nullable = false, columnDefinition = "DECIMAL(5,2) UNSIGNED")
    private BigDecimal weight; // kg

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private int count;

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private int supportCount;

    @Getter(AccessLevel.NONE)
    private LocalDateTime deletedAt;

    public static ExerciseLogDetail create(
            Exercise exercise,
            ExerciseLog exerciseLog,
            int sequence,
            boolean warmUp,
            int intervals,
            BigDecimal weight,
            int count,
            int supportCount
    ) {
        return ExerciseLogDetail.builder()
                .exercise(exercise)
                .exerciseLog(exerciseLog)
                .sequence(sequence)
                .warmUp(warmUp)
                .intervals(intervals)
                .weight(weight)
                .count(count)
                .supportCount(supportCount)
                .build();
    }

    @Builder
    private ExerciseLogDetail(
            Exercise exercise,
            ExerciseLog exerciseLog,
            int sequence,
            boolean warmUp,
            int intervals,
            BigDecimal weight,
            int count,
            int supportCount
    ) {
        this.exercise = exercise;
        this.exerciseLog = exerciseLog;
        this.sequence = sequence;
        this.warmUp = warmUp;
        this.intervals = intervals;
        this.weight = weight;
        this.count = count;
        this.supportCount = supportCount;
    }
}
