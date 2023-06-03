package im.fitdiary.server.security.argumentresolver;

import im.fitdiary.server.security.CustomAuthenticationToken;
import im.fitdiary.server.security.CustomUserDetails;
import im.fitdiary.server.security.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean result = parameter.hasParameterAnnotation(Auth.class)
                && parameter.getParameterType().equals(AuthToken.class);
        log.debug("supportsParameter: {}", result);
        return result;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        AuthToken authToken = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomAuthenticationToken) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            Long id = Long.parseLong(userDetails.getUsername());
            List<GrantedAuthority> grantedAuthorities =
                    new ArrayList<>(userDetails.getAuthorities());
            RoleType roleType = RoleType.from(grantedAuthorities.get(0).getAuthority());

            authToken = new AuthToken(id, roleType);
        }
        log.debug("principal resolved: {}", authToken);
        return authToken;
    }
}
