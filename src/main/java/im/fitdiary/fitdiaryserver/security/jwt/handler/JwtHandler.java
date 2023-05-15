package im.fitdiary.fitdiaryserver.security.jwt.handler;

import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.RoleType;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public interface JwtHandler {

    String createToken(RoleType roleType, String subject) throws NoSuchElementException;

    String getSubject(String token) throws UnauthorizedException;

    RoleType getRoleType(String token) throws UnauthorizedException;

    LocalDateTime getExpiration(String token) throws UnauthorizedException;

    String getType();
}
