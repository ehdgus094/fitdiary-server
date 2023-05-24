package im.fitdiary.fitdiaryserver.exercise.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "deletedAt")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE exercise_log SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(indexes = @Index(columnList = "userId"))
public class ExerciseLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "MEDIUMINT UNSIGNED")
    private int duration; // 운동시간 seconds

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    @Getter(AccessLevel.NONE)
    private LocalDateTime deletedAt;

    public static ExerciseLog create(
            Long userId,
            int duration,
            LocalDateTime measuredAt
    ) {
        return ExerciseLog.builder()
                .userId(userId)
                .duration(duration)
                .measuredAt(measuredAt)
                .build();
    }

    @Builder
    private ExerciseLog(
            Long userId,
            int duration,
            LocalDateTime measuredAt
    ) {
        this.userId = userId;
        this.duration = duration;
        this.measuredAt = measuredAt;
    }
}
