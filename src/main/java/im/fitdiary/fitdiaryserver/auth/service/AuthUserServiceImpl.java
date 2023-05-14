package im.fitdiary.fitdiaryserver.auth.service;

import im.fitdiary.fitdiaryserver.auth.data.AuthUserRepository;
import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.JwtToken;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateAuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.LoginUser;
import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.AuthUserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

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
        createAuthUser.encodePassword(passwordEncoder);
        AuthUser authUser = createAuthUser.toEntity();
        authUserRepository.save(authUser);
        return authUser;
    }

    @Transactional
    public JwtToken login(LoginUser loginUser)
            throws InvalidLoginInfoException, NoSuchElementException {
        AuthUser authUser = authUserRepository
                .findByLoginIdAndLoginType(loginUser.getLoginId(), loginUser.getLoginType())
                .orElseThrow(InvalidLoginInfoException::new);
        if (!loginUser.hasValidPassword(authUser.getPassword(), passwordEncoder)) {
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
    public void delete(Long userId) {
        authUserRepository.deleteByUserId(userId);
    }
}