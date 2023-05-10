package im.fitdiary.fitdiaryserver.user.data.entity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"loginType", "loginId"}))
public class UserAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) BINARY")
    private String loginId;

    @Column(length = 60)
    private String password;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean hasSameToken(String refreshToken) {
        if (this.refreshToken == null) return false;
        return this.refreshToken.equals(refreshToken);
    }

    public boolean isRefreshTokenAboutToExpire(LocalDateTime expiration) {
        // refreshToken 만료 한달 이내 재발급
        LocalDateTime now = LocalDateTime.now();
        return now.plusMonths(1).isAfter(expiration);
    }

    public static UserAuth createEmailAuth(String loginId, String password) {
        return new UserAuth(null, LoginType.EMAIL, loginId, password, null);
    }

    public static UserAuth createKakaoAuth(String loginId) {
        return new UserAuth(null, LoginType.KAKAO, loginId, null, null);
    }

    private UserAuth(Long id, LoginType loginType, String loginId, String password, String refreshToken) {
        this.id = id;
        this.loginType = loginType;
        this.loginId = loginId;
        this.password = password;
        this.refreshToken = refreshToken;
    }
}
