package im.fitdiary.fitdiaryserver.security.jwt.filter;

import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String subject = null;
        RoleType roleType = null;
        String token = request.getHeader("Authorization");

        if (token != null) {
            try {
                subject = jwtHandler.getSubject(token);
                roleType = jwtHandler.getRoleType(token);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(createAuthentication(subject, roleType.toString()));
            } catch (UnauthorizedException ignored) {}
        }

        log.info("authenticated: [roleType={}, id={}]", roleType, subject);
        chain.doFilter(request, response);
    }

    private CustomAuthenticationToken createAuthentication(String id, String roleType) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(roleType));
        CustomUserDetails userDetails = new CustomUserDetails(id, authorities);
        return new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails);
    }
}