package im.fitdiary.fitdiaryserver.user.controller;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
import im.fitdiary.fitdiaryserver.user.dto.CreateEmailUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserReq;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
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

    @PostMapping("/login")
    public Response login(@RequestBody @Valid LoginUserReq req) {
        LoginUserRes loginUser = userService.login(req.getLoginId(), req.getPassword());
        return Response.success(loginUser);
    }

    @GetMapping
    public Response find(@UserId Long id) {
        UserRes user = userService.findById(id);
        return Response.success(user);
    }
}
