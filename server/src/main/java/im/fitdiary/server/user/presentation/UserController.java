package im.fitdiary.server.user.presentation;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e404.UserNotFoundException;
import im.fitdiary.server.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import im.fitdiary.server.user.data.entity.User;
import im.fitdiary.server.user.presentation.dto.*;
import im.fitdiary.server.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public Response<?> createEmail(@RequestBody @Valid CreateEmailUserReq req)
            throws AuthUserDuplicatedException {
        userService.create(req.toDto());
        return new Response<>();
    }

    @PostMapping("/kakao")
    public Response<?> createKakao(@RequestBody @Valid CreateKakaoUserReq req)
            throws AuthUserDuplicatedException {
        userService.create(req.toDto());
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping
    public Response<UserRes> findById(@Auth AuthToken authToken) throws UserNotFoundException {
        User user = userService.findById(authToken.getId());
        return new Response<>(new UserRes(user));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping
    public Response<?> updateById(@Auth AuthToken authToken, @RequestBody @Valid UpdateUserReq req)
            throws UserNotFoundException {
        userService.updateById(authToken.getId(), req.toEditor());
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping
    public Response<?> deleteById(@Auth AuthToken authToken) {
        userService.deleteById(authToken.getId());
        return new Response<>();
    }
}
