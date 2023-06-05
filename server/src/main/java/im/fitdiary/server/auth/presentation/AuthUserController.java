package im.fitdiary.server.auth.presentation;

import im.fitdiary.server.auth.presentation.dto.LoginEmailUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginKakaoUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginUserRes;
import im.fitdiary.server.auth.presentation.dto.RefreshTokenUserRes;
import im.fitdiary.server.auth.service.AuthUserService;
import im.fitdiary.server.auth.service.dto.JwtToken;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e401.InvalidLoginInfoException;
import im.fitdiary.server.exception.e401.UnauthorizedException;
import im.fitdiary.server.exception.e404.AuthUserNotFoundException;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Tag(name = "AuthUser")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/auth/user")
@RestController
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/login/email")
    public Response<LoginUserRes> loginEmailUser(@RequestBody @Valid LoginEmailUserReq req)
            throws InvalidLoginInfoException, NoSuchElementException {
        JwtToken token = authUserService.login(req.toDto());
        return new Response<>(new LoginUserRes(token));
    }

    @PostMapping("/login/kakao")
    public Response<LoginUserRes> loginKakaoUser(@RequestBody @Valid LoginKakaoUserReq req)
            throws InvalidLoginInfoException, NoSuchElementException {
        JwtToken token = authUserService.login(req.toDto());
        return new Response<>(new LoginUserRes(token));
    }

    @Secured("ROLE_USER_ACCESS")
    @PostMapping("/logout")
    public Response<?> logoutUser(@Auth AuthToken authToken)
            throws AuthUserNotFoundException {
        authUserService.logout(authToken.getId());
        return new Response<>();
    }

    @Secured("ROLE_USER_REFRESH")
    @PostMapping("/refresh-token")
    public Response<RefreshTokenUserRes> refreshTokenUser(
            @Auth AuthToken authToken,
            @RequestHeader("Authorization") String refreshToken
    ) throws UnauthorizedException {
        JwtToken token = authUserService.refreshToken(authToken.getId(), refreshToken);
        return new Response<>(new RefreshTokenUserRes(token));
    }
}
