package im.fitdiary.fitdiaryserver.common.interceptor;

import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Slf4j
public class RequestInfoLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String thread = Thread.currentThread().getName();
        String ip = getIp(request);
        String auth = getAuth();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("user-agent");
        log.info("[{}] [{}] [{}] {} {} | {}", thread, ip, auth, method, uri, userAgent);
        return true;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null) ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null) ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null) ip = request.getRemoteAddr();
        return ip;
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomAuthenticationToken) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            ArrayList<GrantedAuthority> authorities =
                    new ArrayList<>(userDetails.getAuthorities());
            String roleType = authorities.get(0).getAuthority();
            String userId = userDetails.getUsername();
            return roleType + "_" + userId;
        }
        return "noAuth";
    }
}
