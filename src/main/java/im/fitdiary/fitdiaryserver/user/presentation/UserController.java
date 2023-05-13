package im.fitdiary.fitdiaryserver.user.presentation;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.presentation.dto.*;
import im.fitdiary.fitdiaryserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
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

    @Secured("ROLE_USER_ACCESS")
    @GetMapping
    public Response find(@UserId Long userId) {
        User user = userService.findById(userId);
        return Response.success(new UserRes(user));
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping
    public Response delete(@UserId Long userId) {
        userService.delete(userId);
        return Response.success();
    }
}
