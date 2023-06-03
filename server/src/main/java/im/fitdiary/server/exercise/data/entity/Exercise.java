package im.fitdiary.server.exercise.data.entity;

import im.fitdiary.server.common.entity.BaseEntity;
import im.fitdiary.server.exercise.data.dto.ExerciseEditor;
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
@SQLDelete(sql = "UPDATE exercise SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(indexes = @Index(columnList = "userId"))
public class Exercise extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseCategory category;

    @Column(nullable = false)
    private boolean active;

    @Getter(AccessLevel.NONE)
    private LocalDateTime deletedAt;

    public void update(ExerciseEditor editor) {
        if (editor.getName().isPresent()) name = editor.getName().get();
        if (editor.getCategory().isPresent()) category = editor.getCategory().get();
        if (editor.getActive().isPresent()) active = editor.getActive().get();
    }

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
