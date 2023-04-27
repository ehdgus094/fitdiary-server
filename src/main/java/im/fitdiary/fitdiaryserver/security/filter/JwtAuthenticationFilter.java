package im.fitdiary.fitdiaryserver.security.filter;

import im.fitdiary.fitdiaryserver.security.jwt.service.JwtService;
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

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // req 헤더에서 토큰 추출
        this.extractToken(request).ifPresent(token -> {
            // jwt claims 추출
            jwtService.extract(token).ifPresent(claims ->
                    this.setAuthentication(claims.getSubject(), claims.getAudience())
            );
        });
        chain.doFilter(request, response);
    }

    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest) request).getHeader("Authorization"));
    }

    private void setAuthentication(String id, String audience) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(audience));
        CustomUserDetails userDetails = new CustomUserDetails(id, authorities);
        SecurityContextHolder
                .getContext()
                .setAuthentication(new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails));
    }
}