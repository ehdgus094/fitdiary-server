package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.UserDuplicatedException;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.UserRepository;
import im.fitdiary.fitdiaryserver.user.service.dto.AuthToken;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;
import im.fitdiary.fitdiaryserver.user.service.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtHandler jwtHandler;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(CreateUser createUser) throws UserDuplicatedException {
        userRepository.findByLoginIdAndLoginType(createUser.getLoginId(), createUser.getLoginType())
                        .ifPresent(user -> {
                            throw new UserDuplicatedException();
                        });
        createUser.encodePassword(passwordEncoder);
        User user = createUser.toEntity();
        userRepository.save(user);
        return user;
    }

    @Transactional
    public AuthToken login(LoginUser loginUser)
            throws InvalidLoginInfoException, NoSuchElementException {
        User user = userRepository
                .findByLoginIdAndLoginType(loginUser.getLoginId(), loginUser.getLoginType())
                .orElseThrow(InvalidLoginInfoException::new);
        if (!loginUser.hasValidPassword(user.getAuth().getPassword(), passwordEncoder)) {
            throw new InvalidLoginInfoException();
        }

        String accessToken =
                jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, user.getId().toString());
        String refreshToken =
                jwtHandler.createToken(RoleType.ROLE_USER_REFRESH, user.getId().toString());
        user.getAuth().updateRefreshToken(refreshToken);
        return new AuthToken(accessToken, refreshToken);
    }

    @Transactional
    public void logout(Long userId) throws UserNotFoundException {
        User user = userRepository.findAuthByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
        user.getAuth().updateRefreshToken(null);
    }

    @Transactional
    public AuthToken refreshToken(Long userId, String refreshToken) throws UnauthorizedException {
        User user = userRepository.findAuthByUserId(userId)
                .orElseThrow(UnauthorizedException::new);
        if (!user.getAuth().hasSameToken(refreshToken)) {
            throw new UnauthorizedException();
        }

        LocalDateTime expiration = jwtHandler.getExpiration(user.getAuth().getRefreshToken());
        if (user.getAuth().isRefreshTokenAboutToExpire(expiration)) {
            String newRefreshToken =
                    jwtHandler.createToken(RoleType.ROLE_USER_REFRESH, user.getId().toString());
            user.getAuth().updateRefreshToken(newRefreshToken);
        }
        String newAccessToken =
                jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, user.getId().toString());

        return new AuthToken(newAccessToken, user.getAuth().getRefreshToken());
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void deleteById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
