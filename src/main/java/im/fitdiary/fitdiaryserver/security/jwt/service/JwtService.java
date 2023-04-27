package im.fitdiary.fitdiaryserver.security.jwt.service;

import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import io.jsonwebtoken.Claims;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface JwtService {
    String createToken(RoleType roleType, String subject) throws NoSuchElementException;
    Optional<Claims> extract(String token);
    String getType();
}
