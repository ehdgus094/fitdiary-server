package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.security.jwt.service.JwtService;
import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.RefreshTokenRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.repository.UserRepository;
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
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void create(User user) {
        user.getAuth().passwordEncode(passwordEncoder.encode(user.getAuth().getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public LoginUserRes login(String loginId, String password)
            throws InvalidLoginInfoException, NoSuchElementException {
        User user = userRepository
                .findByLoginId(loginId)
                .orElseThrow(InvalidLoginInfoException::new);
        if (!passwordEncoder.matches(password, user.getAuth().getPassword())) {
            throw new InvalidLoginInfoException();
        }

        String accessToken =
                jwtService.createToken(RoleType.ROLE_USER_ACCESS, user.getId().toString());
        String refreshToken =
                jwtService.createToken(RoleType.ROLE_USER_REFRESH, user.getId().toString());
        user.getAuth().updateRefreshToken(refreshToken);
        return new LoginUserRes(accessToken, refreshToken, user);
    }

    @Transactional
    @Override
    public void logout(Long userId) throws UserNotFoundException {
        User user = userRepository.findAuthByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
        user.getAuth().updateRefreshToken(null);
    }

    @Transactional
    @Override
    public RefreshTokenRes refreshToken(Long userId, String refreshToken) throws UnauthorizedException {
        User user = userRepository.findAuthByUserId(userId)
                .orElseThrow(UnauthorizedException::new);
        if (!user.getAuth().getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException();
        }

        String newAccessToken =
                jwtService.createToken(RoleType.ROLE_USER_ACCESS, user.getId().toString());

        // refreshToken 만료 한달 이내 재발급
        LocalDateTime expiration = jwtService.getExpiration(refreshToken);
        LocalDateTime now = LocalDateTime.now();
        if (now.plusMonths(1).isAfter(expiration)) {
            String newRefreshToken =
                    jwtService.createToken(RoleType.ROLE_USER_REFRESH, user.getId().toString());
            user.getAuth().updateRefreshToken(newRefreshToken);
        }

        return new RefreshTokenRes(newAccessToken, user.getAuth().getRefreshToken());
    }

    @Transactional(readOnly = true)
    @Override
    public UserRes findById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return new UserRes(user);
    }

    @Transactional
    @Override
    public void deleteById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
