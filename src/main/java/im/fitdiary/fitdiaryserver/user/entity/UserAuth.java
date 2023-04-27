package im.fitdiary.fitdiaryserver.user.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "loginType", "loginId", "password", "refreshToken"})
public class UserAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) BINARY")
    private String loginId;

    @Column(length = 60)
    private String password;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void passwordEncode(String encoded) {
        this.password = encoded;
    }

    @Builder
    public UserAuth(LoginType loginType, String loginId, String password) {
        Assert.isInstanceOf(LoginType.class, loginType, "LoginType must not be empty");
        Assert.hasText(loginId, "loginId must not be empty");
        if (loginType.equals(LoginType.EMAIL)) {
            Assert.hasText(password, "password must not be empty");
        } else {
            Assert.isNull(password, "password must be empty");
        }

        this.loginType = loginType;
        this.loginId = loginId;
        this.password = password;
    }
}
