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
@ToString
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE exercise SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(indexes = @Index(columnList = "userId"))
public class Exercise extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Setter(AccessLevel.PROTECTED)
    @Column(nullable = false)
    private String name;

    @Setter(AccessLevel.PROTECTED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseCategory category;

    @Setter(AccessLevel.PROTECTED)
    @Column(nullable = false)
    private boolean active;

    @Getter(AccessLevel.NONE)
    private LocalDateTime deletedAt;

    public static Exercise create(Long userId, String name, ExerciseCategory category, boolean active) {
        return Exercise.builder()
                .userId(userId)
                .name(name)
                .category(category)
                .active(active)
                .build();
    }

    @Builder
    private Exercise(
            Long userId,
            String name,
            ExerciseCategory category,
            boolean active
    ) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.active = active;
    }
}
