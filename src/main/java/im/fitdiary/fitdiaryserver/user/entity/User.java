package im.fitdiary.fitdiaryserver.user.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "name", "birthYmd", "gender", "height", "weight", "deletedAt"})
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

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer height;

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer weight;

    private LocalDateTime deletedAt;

    @Builder
    public User(UserAuth auth, String name, String birthYmd, Gender gender, Integer height, Integer weight) {
        Assert.hasText(name, "name must not be empty");
        Assert.hasText(birthYmd, "birthYmd must not be empty");
        Assert.isInstanceOf(Gender.class, gender, "gender must not be empty");
        Assert.notNull(height, "height must not be empty");
        Assert.notNull(weight, "weight must not be empty");
        this.auth = auth;
        this.name = name;
        this.birthYmd = birthYmd;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }
}
