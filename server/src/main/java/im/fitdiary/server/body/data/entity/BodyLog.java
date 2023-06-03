package im.fitdiary.server.body.data.entity;

import im.fitdiary.server.body.data.dto.BodyLogEditor;
import im.fitdiary.server.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "deletedAt")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE body_log SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(indexes = @Index(columnList = "userId"))
public class BodyLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "DECIMAL(7,4) UNSIGNED")
    private BigDecimal height; // cm

    @Column(nullable = false, columnDefinition = "DECIMAL(7,4) UNSIGNED")
    private BigDecimal weight; // kg

    @Nullable
    @Column(columnDefinition = "DECIMAL(7,4) UNSIGNED")
    private BigDecimal muscleMass; // kg

    @Nullable
    @Column(columnDefinition = "DECIMAL(5,2) UNSIGNED")
    private BigDecimal bodyFat; // %

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    @Getter(AccessLevel.NONE)
    private LocalDateTime deletedAt;

    public void update(BodyLogEditor editor) {
        if (editor.getHeight().isPresent()) height = editor.getHeight().get();
        if (editor.getWeight().isPresent()) weight = editor.getWeight().get();
        if (editor.getMuscleMass().isPresent()) muscleMass = editor.getMuscleMass().get();
        if (editor.getBodyFat().isPresent()) bodyFat = editor.getBodyFat().get();
        if (editor.getMeasuredAt().isPresent()) measuredAt = editor.getMeasuredAt().get();
    }

    public static BodyLog create(
            Long userId,
            BigDecimal height,
            BigDecimal weight,
            @Nullable BigDecimal muscleMass,
            @Nullable BigDecimal bodyFat,
            LocalDateTime measuredAt
    ) {
        return BodyLog.builder()
                .userId(userId)
                .height(height)
                .weight(weight)
                .muscleMass(muscleMass)
                .bodyFat(bodyFat)
                .measuredAt(measuredAt)
                .build();
    }

    @Builder
    private BodyLog(
            Long userId,
            BigDecimal height,
            BigDecimal weight,
            @Nullable BigDecimal muscleMass,
            @Nullable BigDecimal bodyFat,
            LocalDateTime measuredAt
    ) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.measuredAt = measuredAt;
    }
}
