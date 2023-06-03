package im.fitdiary.server.auth.service;

import im.fitdiary.server.auth.data.AuthUserRepository;
import im.fitdiary.server.auth.data.entity.AuthUser;
import im.fitdiary.server.auth.service.dto.JwtToken;
import im.fitdiary.server.auth.service.dto.CreateAuthUser;
import im.fitdiary.server.auth.service.dto.LoginUser;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.exception.e401.InvalidLoginInfoException;
import im.fitdiary.server.exception.e401.UnauthorizedException;
import im.fitdiary.server.exception.e404.AuthUserNotFoundException;
import im.fitdiary.server.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.server.security.RoleType;
import im.fitdiary.server.security.jwt.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;

    private final JwtHandler jwtHandler;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthUser create(CreateAuthUser createAuthUser) throws AuthUserDuplicatedException {
        authUserRepository
                .findByLoginIdAndLoginTypeOrUserId(
                        createAuthUser.getLoginId(),
                        createAuthUser.getLoginType(),
                        createAuthUser.getUserId()
                ).ifPresent(authUser -> {
                    throw new AuthUserDuplicatedException();
                });
        AuthUser authUser = createAuthUser.toEntity();
        authUser.encodePassword(passwordEncoder);
        authUserRepository.save(authUser);
        return authUser;
    }

    @Transactional
    public JwtToken login(LoginUser loginUser)
            throws InvalidLoginInfoException, NoSuchElementException {
        AuthUser authUser = authUserRepository
                .findByLoginIdAndLoginType(loginUser.getLoginId(), loginUser.getLoginType())
                .orElseThrow(InvalidLoginInfoException::new);
        if (!authUser.isValidPassword(passwordEncoder, loginUser.getPassword())) {
            throw new InvalidLoginInfoException();
        }

        String accessToken =
                jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, authUser.getUserId().toString());
        String refreshToken =
                jwtHandler.createToken(RoleType.ROLE_USER_REFRESH, authUser.getUserId().toString());
        authUser.updateRefreshToken(refreshToken);
        return new JwtToken(accessToken, refreshToken);
    }

    @Transactional
    public void logout(Long userId) throws AuthUserNotFoundException {
        AuthUser authUser = authUserRepository.findByUserId(userId)
                .orElseThrow(AuthUserNotFoundException::new);
        authUser.updateRefreshToken(null);
    }

    @Transactional
    public JwtToken refreshToken(Long userId, String refreshToken) throws UnauthorizedException {
        AuthUser authUser = authUserRepository.findByUserId(userId)
                .orElseThrow(UnauthorizedException::new);
        if (!authUser.hasSameToken(refreshToken)) {
            throw new UnauthorizedException();
        }

        LocalDateTime expiration = jwtHandler.getExpiration(authUser.getRefreshToken());
        if (authUser.isRefreshTokenAboutToExpire(expiration)) {
            String newRefreshToken =
                    jwtHandler.createToken(RoleType.ROLE_USER_REFRESH, authUser.getUserId().toString());
            authUser.updateRefreshToken(newRefreshToken);
        }
        String newAccessToken =
                jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, authUser.getUserId().toString());

        return new JwtToken(newAccessToken, authUser.getRefreshToken());
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        authUserRepository.deleteByUserId(userId);
    }
}
