package im.fitdiary.server.security.jwt.handler;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.config.ConfigProperties;
import im.fitdiary.server.exception.e401.UnauthorizedException;
import im.fitdiary.server.security.RoleType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class JwtHandlerImpl implements JwtHandler {

    private static final String TYPE = "Bearer ";

    private final ConfigProperties properties;

    public String createToken(RoleType roleType, String subject) throws NoSuchElementException {
        String secret = getSecret(roleType).orElseThrow();
        Long maxAgeSeconds = getMaxAgeSeconds(roleType).orElseThrow();
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();

        return TYPE + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getSubject(String token) throws UnauthorizedException {
        return extract(token)
                .orElseThrow(UnauthorizedException::new)
                .getSubject();
    }

    public RoleType getRoleType(String token) throws UnauthorizedException {
        String audience = extract(token)
                .orElseThrow(UnauthorizedException::new)
                .getAudience();
        RoleType roleType = RoleType.from(audience);
        if (roleType == null) throw new UnauthorizedException();
        return roleType;
    }

    public LocalDateTime getExpiration(String token) throws UnauthorizedException {
        return new Timestamp(extract(token)
                .orElseThrow(UnauthorizedException::new)
                .getExpiration()
                .getTime()
        ).toLocalDateTime();
    }

    public String getType() {
        return TYPE;
    }

    private Optional<Claims> extract(String token) {
        for (RoleType roleType : RoleType.values()) {
            try {
                String secret = getSecret(roleType).orElseThrow();
                return Optional.of(Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(unType(token))
                        .getBody()
                        .setAudience(roleType.toString()));
            } catch (JwtException | NoSuchElementException ignored) {}
        }
        return Optional.empty();
    }

    private String unType(String token) throws JwtException {
        if (token == null) throw new JwtException("wrong type");
        if (token.contains(TYPE)) return token.substring(TYPE.length());
        throw new JwtException("wrong type");
    }

    private Optional<String> getSecret(RoleType roleType) {
        switch (roleType) {
            case ROLE_USER_ACCESS:
                return Optional.ofNullable(properties.getJwt().getUser().getAccess().getSecret());
            case ROLE_USER_REFRESH:
                return Optional.ofNullable(properties.getJwt().getUser().getRefresh().getSecret());
            default:
                return Optional.empty();
        }
    }

    private Optional<Long> getMaxAgeSeconds(RoleType roleType) {
        switch (roleType) {
            case ROLE_USER_ACCESS:
                return Optional.ofNullable(properties.getJwt().getUser().getAccess().getMaxAge());
            case ROLE_USER_REFRESH:
                return Optional.ofNullable(properties.getJwt().getUser().getRefresh().getMaxAge());
            default:
                return Optional.empty();
        }
    }
}
