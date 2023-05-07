package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.UserDuplicatedException;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.service.dto.AuthToken;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;
import im.fitdiary.fitdiaryserver.user.service.dto.LoginUser;

import java.util.NoSuchElementException;

public interface UserService {
    User create(CreateUser createUser) throws UserDuplicatedException;
    AuthToken login(LoginUser loginUser) throws InvalidLoginInfoException, NoSuchElementException;
    void logout(Long userId) throws UserNotFoundException;
    AuthToken refreshToken(Long userId, String refreshToken) throws UnauthorizedException;
    User findById(Long userId) throws UserNotFoundException;
    void deleteById(Long userId) throws UserNotFoundException;
}
