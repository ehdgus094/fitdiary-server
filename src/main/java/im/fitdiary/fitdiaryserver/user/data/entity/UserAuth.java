package im.fitdiary.fitdiaryserver.user.data.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

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

    @Nullable
    @Column(length = 60)
    private String password;

    @Nullable
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
        return UserAuth.builder()
                .loginType(LoginType.EMAIL)
                .loginId(loginId)
                .password(password)
                .refreshToken(null)
                .build();
    }

    public static UserAuth createKakaoAuth(String loginId) {
        return UserAuth.builder()
                .loginType(LoginType.KAKAO)
                .loginId(loginId)
                .password(null)
                .refreshToken(null)
                .build();
    }

    @Builder
    private UserAuth(
            LoginType loginType,
            String loginId,
            @Nullable String password,
            @Nullable String refreshToken
    ) {
        this.loginType = loginType;
        this.loginId = loginId;
        this.password = password;
        this.refreshToken = refreshToken;
    }
}
