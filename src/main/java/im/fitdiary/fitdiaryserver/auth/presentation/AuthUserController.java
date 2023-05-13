package im.fitdiary.fitdiaryserver.auth.presentation;

import im.fitdiary.fitdiaryserver.auth.presentation.dto.LoginEmailUserReq;
import im.fitdiary.fitdiaryserver.auth.presentation.dto.LoginKakaoUserReq;
import im.fitdiary.fitdiaryserver.auth.presentation.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.auth.presentation.dto.RefreshTokenUserRes;
import im.fitdiary.fitdiaryserver.auth.service.AuthUserService;
import im.fitdiary.fitdiaryserver.auth.service.dto.AuthToken;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth/user")
@RestController
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/login/email")
    public Response loginEmailUser(@RequestBody @Valid LoginEmailUserReq req) {
        AuthToken token = authUserService.login(req.toServiceDto());
        return Response.success(new LoginUserRes(token));
    }

    @PostMapping("/login/kakao")
    public Response loginKakaoUser(@RequestBody @Valid LoginKakaoUserReq req) {
        AuthToken token = authUserService.login(req.toServiceDto());
        return Response.success(new LoginUserRes(token));
    }

    @Secured("ROLE_USER_ACCESS")
    @PostMapping("/logout")
    public Response logoutUser(@UserId Long userId) {
        authUserService.logout(userId);
        return Response.success();
    }

    @Secured("ROLE_USER_REFRESH")
    @PostMapping("/refresh-token")
    public Response refreshTokenUser(@UserId Long userId, @RequestHeader("Authorization") String refreshToken) {
        AuthToken token = authUserService.refreshToken(userId, refreshToken);
        return Response.success(new RefreshTokenUserRes(token));
    }
}
