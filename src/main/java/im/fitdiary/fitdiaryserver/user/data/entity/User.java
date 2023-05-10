package im.fitdiary.fitdiaryserver.user.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"auth"})
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "auth_id")
    private UserAuth auth;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 8)
    private String birthYmd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String email;

    private LocalDateTime deletedAt;

    public static User create(UserAuth auth, String name, String birthYmd, Gender gender, String email) {
        return new User(auth, name, birthYmd, gender, email);
    }

    private User(UserAuth auth, String name, String birthYmd, Gender gender, String email) {
        this.auth = auth;
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
        this.email = email;
    }
}
