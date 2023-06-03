package im.fitdiary.server.user.presentation;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import im.fitdiary.server.user.data.entity.User;
import im.fitdiary.server.user.presentation.dto.*;
import im.fitdiary.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public Response createEmail(@RequestBody @Valid CreateEmailUserReq req) {
        userService.create(req.toDto());
        return Response.success();
    }

    @PostMapping("/kakao")
    public Response createKakao(@RequestBody @Valid CreateKakaoUserReq req) {
        userService.create(req.toDto());
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping
    public Response findById(@Auth AuthToken authToken) {
        User user = userService.findById(authToken.getId());
        return Response.success(new UserRes(user));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping
    public Response updateById(@Auth AuthToken authToken, @RequestBody @Valid UpdateUserReq req) {
        userService.updateById(authToken.getId(), req.toEditor());
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping
    public Response deleteById(@Auth AuthToken authToken) {
        userService.deleteById(authToken.getId());
        return Response.success();
    }
}
