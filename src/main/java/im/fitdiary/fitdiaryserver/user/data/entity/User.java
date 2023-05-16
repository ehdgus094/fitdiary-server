package im.fitdiary.fitdiaryserver.user.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import im.fitdiary.fitdiaryserver.user.data.dto.UserEditor;
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
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 8)
    private String birthYmd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String email;

    @Getter(AccessLevel.NONE)
    private LocalDateTime deletedAt;

    public void update(UserEditor editor) {
        if (editor.getName().isPresent()) name = editor.getName().get();
    }

    public static User create(String name, String birthYmd, Gender gender, String email) {
        return User.builder()
                .name(name)
                .birthYmd(birthYmd)
                .gender(gender)
                .email(email)
                .build();
    }

    @Builder
    private User(
            String name,
            String birthYmd,
            Gender gender,
            String email
    ) {
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
        this.email = email;
    }
}
