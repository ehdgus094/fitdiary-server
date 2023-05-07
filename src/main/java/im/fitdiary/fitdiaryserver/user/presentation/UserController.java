package im.fitdiary.fitdiaryserver.user.presentation;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.presentation.dto.*;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import im.fitdiary.fitdiaryserver.user.service.dto.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public Response createEmail(@RequestBody @Valid CreateEmailUserReq req) {
        userService.create(req.toServiceDto());
        return Response.success();
    }

    @PostMapping("/kakao")
    public Response createKakao(@RequestBody @Valid CreateKakaoUserReq req) {
        userService.create(req.toServiceDto());
        return Response.success();
    }

    @PostMapping("/login/email")
    public Response loginEmail(@RequestBody @Valid LoginEmailUserReq req) {
        AuthToken token = userService.login(req.toServiceDto());
        return Response.success(new LoginUserRes(token));
    }

    @PostMapping("/login/kakao")
    public Response loginKakao(@RequestBody @Valid LoginKakaoUserReq req) {
        AuthToken token = userService.login(req.toServiceDto());
        return Response.success(new LoginUserRes(token));
    }

    @PostMapping("/logout")
    public Response logout(@UserId Long id) {
        userService.logout(id);
        return Response.success();
    }

    @PostMapping("/refresh-token")
    public Response refreshToken(@UserId Long id, @RequestHeader("Authorization") String refreshToken) {
        AuthToken token = userService.refreshToken(id, refreshToken);
        return Response.success(new RefreshTokenUserRes(token));
    }

    @GetMapping
    public Response find(@UserId Long id) {
        User user = userService.findById(id);
        return Response.success(new UserRes(user));
    }

    @DeleteMapping
    public Response remove(@UserId Long id) {
        userService.deleteById(id);
        return Response.success();
    }
}
