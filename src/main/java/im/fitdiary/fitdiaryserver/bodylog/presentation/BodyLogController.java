package im.fitdiary.fitdiaryserver.bodylog.presentation;

import im.fitdiary.fitdiaryserver.bodylog.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.bodylog.service.BodyLogService;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/body-log")
@RestController
public class BodyLogController {

    private final BodyLogService bodyLogService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response create(@UserId Long id, @RequestBody @Valid CreateBodyLogReq req) {
        bodyLogService.create(id, req.toServiceDto());
        return Response.success();
    }
}
