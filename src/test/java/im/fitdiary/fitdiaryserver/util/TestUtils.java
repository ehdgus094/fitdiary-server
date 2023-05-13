package im.fitdiary.fitdiaryserver.util;

import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import im.fitdiary.fitdiaryserver.security.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Set;

public class TestUtils {

    public static void setCustomAuthenticationToken() {
        // @Auth AuthToken을 생성하기위해 사용

        long id = 1L;
        RoleType roleType = RoleType.ROLE_USER_ACCESS;

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(roleType.toString()));
        CustomUserDetails userDetails = new CustomUserDetails(Long.toString(id), authorities);
        CustomAuthenticationToken authenticationToken =
                new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
