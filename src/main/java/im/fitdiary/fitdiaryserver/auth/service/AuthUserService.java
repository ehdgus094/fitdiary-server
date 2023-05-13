package im.fitdiary.fitdiaryserver.auth.service;

import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.JwtToken;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateAuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.LoginUser;
import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.AuthUserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;

import java.util.NoSuchElementException;

public interface AuthUserService {
    AuthUser create(CreateAuthUser createAuthUser) throws AuthUserDuplicatedException;
    JwtToken login(LoginUser loginUser) throws InvalidLoginInfoException, NoSuchElementException;
    void logout(Long userId) throws AuthUserNotFoundException;
    JwtToken refreshToken(Long userId, String refreshToken) throws UnauthorizedException;
    void delete(Long userId);
}
