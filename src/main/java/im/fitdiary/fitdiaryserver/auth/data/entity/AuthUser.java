package im.fitdiary.fitdiaryserver.auth.data.entity;

import im.fitdiary.fitdiaryserver.common.entity.BaseEntity;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId", unique = true),
        @Index(columnList = "loginType, loginId", unique = true)
})
public class AuthUser extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLoginType loginType;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) BINARY")
    private String loginId;

    @Nullable
    @Column(length = 60)
    private String password;

    @Nullable
    private String refreshToken;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        if (UserLoginType.EMAIL.equals(loginType)) {
            password = passwordEncoder.encode(password);
        } else {
            password = null;
        }
    }

    public boolean isValidPassword(PasswordEncoder passwordEncoder, String inputPassword) {
        if (!UserLoginType.EMAIL.equals(loginType)) return true;
        return passwordEncoder.matches(inputPassword, password);
    }

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

    public static AuthUser createEmailAuth(Long userId, String loginId, String password) {
        return AuthUser.builder()
                .userId(userId)
                .loginType(UserLoginType.EMAIL)
                .loginId(loginId.toLowerCase())
                .password(password)
                .refreshToken(null)
                .build();
    }

    public static AuthUser createKakaoAuth(Long userId, String loginId) {
        return AuthUser.builder()
                .userId(userId)
                .loginType(UserLoginType.KAKAO)
                .loginId(loginId)
                .password(null)
                .refreshToken(null)
                .build();
    }

    @Builder
    private AuthUser(
            Long userId,
            UserLoginType loginType,
            String loginId,
            @Nullable String password,
            @Nullable String refreshToken
    ) {
        this.userId = userId;
        this.loginType = loginType;
        this.loginId = loginId;
        this.password = password;
        this.refreshToken = refreshToken;
    }
}
