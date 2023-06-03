package im.fitdiary.server.auth.service;

import im.fitdiary.server.auth.data.entity.AuthUser;
import im.fitdiary.server.auth.service.dto.JwtToken;
import im.fitdiary.server.auth.service.dto.CreateAuthUser;
import im.fitdiary.server.auth.service.dto.LoginUser;
import im.fitdiary.server.exception.e401.InvalidLoginInfoException;
import im.fitdiary.server.exception.e401.UnauthorizedException;
import im.fitdiary.server.exception.e404.AuthUserNotFoundException;
import im.fitdiary.server.exception.e409.AuthUserDuplicatedException;

import java.util.NoSuchElementException;

public interface AuthUserService {

    AuthUser create(CreateAuthUser createAuthUser) throws AuthUserDuplicatedException;

    JwtToken login(LoginUser loginUser) throws InvalidLoginInfoException, NoSuchElementException;

    void logout(Long userId) throws AuthUserNotFoundException;

    JwtToken refreshToken(Long userId, String refreshToken) throws UnauthorizedException;

    void deleteByUserId(Long userId);
}
