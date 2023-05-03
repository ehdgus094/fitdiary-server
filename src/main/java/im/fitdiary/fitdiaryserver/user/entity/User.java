package im.fitdiary.fitdiaryserver.user.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "name", "birthYmd", "gender", "deletedAt"})
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auth_id", nullable = false)
    private UserAuth auth;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 8)
    private String birthYmd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private LocalDateTime deletedAt;

    public static User create(UserAuth auth, String name, String birthYmd, Gender gender) {
        return new User(auth, name, birthYmd, gender);
    }

    private User(UserAuth auth, String name, String birthYmd, Gender gender) {
        this.auth = auth;
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
    }
}
