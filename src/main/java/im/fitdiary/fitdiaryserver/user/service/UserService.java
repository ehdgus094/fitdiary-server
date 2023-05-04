package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.RefreshTokenRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.User;

import java.util.NoSuchElementException;

public interface UserService {
    void create(User user);
    LoginUserRes login(String loginId, String password) throws InvalidLoginInfoException, NoSuchElementException;
    void logout(Long userId) throws UserNotFoundException;
    RefreshTokenRes refreshToken(Long userId, String refreshToken) throws UnauthorizedException;
    UserRes findById(Long userId) throws UserNotFoundException;
    void deleteById(Long userId) throws UserNotFoundException;
}
