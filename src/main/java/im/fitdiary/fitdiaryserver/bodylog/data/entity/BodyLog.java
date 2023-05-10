package im.fitdiary.fitdiaryserver.bodylog.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE body_log SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class BodyLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal height; // cm

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal weight; // kg

    @Column(precision = 8, scale = 4)
    private BigDecimal muscleMass; // kg

    @Column(precision = 5, scale = 2)
    private BigDecimal bodyFat; // %

    @Column(nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime measuredAt;

    private LocalDateTime deletedAt;

    public static BodyLog create(
            User user,
            BigDecimal height,
            BigDecimal weight,
            BigDecimal muscleMass,
            BigDecimal bodyFat,
            LocalDateTime measuredAt
    ) {
        return new BodyLog(user, height, weight, muscleMass, bodyFat, measuredAt);
    }

    private BodyLog(
            User user,
            BigDecimal height,
            BigDecimal weight,
            BigDecimal muscleMass,
            BigDecimal bodyFat,
            LocalDateTime measuredAt
    ) {
        this.user = user;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.measuredAt = measuredAt;
    }
}
