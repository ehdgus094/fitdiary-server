package im.fitdiary.fitdiaryserver.user.entity;

import javax.persistence.*;
import lombok.*;

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

    public static UserAuth createEmailAuth(String loginId, String password) {
        return new UserAuth(null, LoginType.EMAIL, loginId, password, null);
    }

    private UserAuth(Long id, LoginType loginType, String loginId, String password, String refreshToken) {
        this.id = id;
        this.loginType = loginType;
        this.loginId = loginId;
        this.password = password;
        this.refreshToken = refreshToken;
    }
}
