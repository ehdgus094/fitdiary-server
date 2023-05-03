package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.NotFoundException;
import im.fitdiary.fitdiaryserver.security.jwt.service.JwtService;
import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    @Override
    public UserRes findById(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));
        return new UserRes(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
