package im.fitdiary.server.auth.presentation;

import im.fitdiary.server.auth.presentation.dto.LoginEmailUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginKakaoUserReq;
import im.fitdiary.server.auth.presentation.dto.LoginUserRes;
import im.fitdiary.server.auth.presentation.dto.RefreshTokenUserRes;
import im.fitdiary.server.auth.service.AuthUserService;
import im.fitdiary.server.auth.service.dto.JwtToken;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/auth/user")
@RestController
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/login/email")
    public Response loginEmailUser(@RequestBody @Valid LoginEmailUserReq req) {
        JwtToken token = authUserService.login(req.toDto());
        return Response.success(new LoginUserRes(token));
    }

    @PostMapping("/login/kakao")
    public Response loginKakaoUser(@RequestBody @Valid LoginKakaoUserReq req) {
        JwtToken token = authUserService.login(req.toDto());
        return Response.success(new LoginUserRes(token));
    }

    @Secured("ROLE_USER_ACCESS")
    @PostMapping("/logout")
    public Response logoutUser(@Auth AuthToken authToken) {
        authUserService.logout(authToken.getId());
        return Response.success();
    }

    @Secured("ROLE_USER_REFRESH")
    @PostMapping("/refresh-token")
    public Response refreshTokenUser(@Auth AuthToken authToken, @RequestHeader("Authorization") String refreshToken) {
        JwtToken token = authUserService.refreshToken(authToken.getId(), refreshToken);
        return Response.success(new RefreshTokenUserRes(token));
    }
}
