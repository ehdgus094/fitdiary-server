package im.fitdiary.fitdiaryserver.body.presentation;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.presentation.dto.BodyLogRes;
import im.fitdiary.fitdiaryserver.body.presentation.dto.BodyLogSliceRes;
import im.fitdiary.fitdiaryserver.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.presentation.dto.UpdateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.service.BodyLogService;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/body/log")
@RestController
public class BodyLogController {

    private final BodyLogService bodyLogService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response create(@Auth AuthToken authToken, @RequestBody @Valid CreateBodyLogReq req) {
        BodyLog bodyLog = bodyLogService.create(req.toDto(authToken.getId()));
        return Response.success(new BodyLogRes(bodyLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/recent")
    public Response findRecent(@Auth AuthToken authToken, Pageable pageable) {
        BodyLogSlice bodyLogSlice = bodyLogService.findRecent(pageable, authToken.getId());
        return Response.success(new BodyLogSliceRes(bodyLogSlice));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping("/{bodyLogId}")
    public Response updateById(
            @Auth AuthToken authToken,
            @PathVariable("bodyLogId") Long bodyLogId,
            @RequestBody @Valid UpdateBodyLogReq req
    ) {
        bodyLogService.updateById(bodyLogId, authToken.getId(), req.toEditor());
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{bodyLogId}")
    public Response deleteById(@Auth AuthToken authToken, @PathVariable("bodyLogId") Long bodyLogId) {
        bodyLogService.deleteById(bodyLogId, authToken.getId());
        return Response.success();
    }
}
