package im.fitdiary.fitdiaryserver.security.jwt.service;

import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface JwtService {
    String createToken(RoleType roleType, String subject) throws NoSuchElementException;
    Optional<Claims> extract(String token);
    LocalDateTime getExpiration(String token) throws UnauthorizedException;
    String getType();
}
