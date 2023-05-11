package im.fitdiary.fitdiaryserver.bodylog.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "user")
@DynamicInsert
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE body_log SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class BodyLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

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
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime measuredAt;

    @Nullable
    private LocalDateTime deletedAt;

    public static BodyLog create(
            User user,
            BigDecimal height,
            BigDecimal weight,
            @Nullable BigDecimal muscleMass,
            @Nullable BigDecimal bodyFat,
            LocalDateTime measuredAt
    ) {
        return BodyLog.builder()
                .user(user)
                .height(height)
                .weight(weight)
                .muscleMass(muscleMass)
                .bodyFat(bodyFat)
                .measuredAt(measuredAt)
                .build();
    }

    @Builder
    private BodyLog(
            User user,
            BigDecimal height,
            BigDecimal weight,
            @Nullable BigDecimal muscleMass,
            @Nullable BigDecimal bodyFat,
            LocalDateTime measuredAt,
            @Nullable LocalDateTime deletedAt
    ) {
        this.user = user;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.measuredAt = measuredAt;
        this.deletedAt = deletedAt;
    }
}
