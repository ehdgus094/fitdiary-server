package im.fitdiary.fitdiaryserver.bodylog.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE body_log SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class BodyLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal height; // cm

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal weight; // kg

    @Column(precision = 8, scale = 4)
    private BigDecimal muscleMass; // kg

    @Column(precision = 5, scale = 2)
    private BigDecimal bodyFat; // %

    private LocalDateTime deletedAt;

    public static BodyLog create(Long userId, BigDecimal height, BigDecimal weight, BigDecimal muscleMass, BigDecimal bodyFat) {
        return new BodyLog(userId, height, weight, muscleMass, bodyFat);
    }

    private BodyLog(Long userId, BigDecimal height, BigDecimal weight, BigDecimal muscleMass, BigDecimal bodyFat) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
    }
}
