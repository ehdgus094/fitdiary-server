package im.fitdiary.fitdiaryserver.user.controller;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
import im.fitdiary.fitdiaryserver.user.dto.*;
import im.fitdiary.fitdiaryserver.user.service.UserService;
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
        userService.create(req.toEntity());
        return Response.success();
    }

    @PostMapping("/login/email")
    public Response loginEmail(@RequestBody @Valid LoginEmailUserReq req) {
        LoginUserRes res = userService.login(req.getLoginId(), req.getPassword());
        return Response.success(res);
    }

    @PostMapping("/logout")
    public Response logout(@UserId Long id) {
        userService.logout(id);
        return Response.success();
    }

    @PostMapping("/refresh-token")
    public Response refreshToken(@UserId Long id, @RequestHeader("Authorization") String refreshToken) {
        RefreshTokenRes res = userService.refreshToken(id, refreshToken);
        return Response.success(res);
    }

    @GetMapping
    public Response find(@UserId Long id) {
        UserRes res = userService.findById(id);
        return Response.success(res);
    }

    @DeleteMapping
    public Response remove(@UserId Long id) {
        userService.deleteById(id);
        return Response.success();
    }
}
