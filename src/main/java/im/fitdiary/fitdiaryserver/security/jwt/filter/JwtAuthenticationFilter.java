package im.fitdiary.fitdiaryserver.security.jwt.filter;

import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        this.extractToken(request).ifPresent(token -> {
            try {
                String subject = jwtHandler.getSubject(token);
                RoleType roleType = jwtHandler.getRoleType(token);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(this.createAuthentication(subject, roleType.toString()));
            } catch (UnauthorizedException ignored) {}
        });
        chain.doFilter(request, response);
    }

    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest) request).getHeader("Authorization"));
    }

    private CustomAuthenticationToken createAuthentication(String id, String roleType) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(roleType));
        CustomUserDetails userDetails = new CustomUserDetails(id, authorities);
        return new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails);
    }
}