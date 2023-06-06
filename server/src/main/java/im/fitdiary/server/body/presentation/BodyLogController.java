package im.fitdiary.server.body.presentation;

import im.fitdiary.server.body.data.entity.BodyLog;
import im.fitdiary.server.body.presentation.dto.BodyLogRes;
import im.fitdiary.server.body.presentation.dto.BodyLogSliceRes;
import im.fitdiary.server.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.server.body.presentation.dto.UpdateBodyLogReq;
import im.fitdiary.server.body.service.BodyLogService;
import im.fitdiary.server.body.service.dto.BodyLogSlice;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e404.BodyLogNotFoundException;
import im.fitdiary.server.exception.e404.PreviousHeightNotFound;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Body - Log")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/body/logs")
@RestController
public class BodyLogController {

    private final BodyLogService bodyLogService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response<BodyLogRes> create(@Auth AuthToken authToken, @RequestBody @Valid CreateBodyLogReq req)
            throws PreviousHeightNotFound {
        BodyLog bodyLog = bodyLogService.create(req.toDto(authToken.getId()));
        return new Response<>(new BodyLogRes(bodyLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping
    public Response<BodyLogSliceRes> find(@Auth AuthToken authToken, Pageable pageable) {
        BodyLogSlice bodyLogSlice = bodyLogService.find(pageable, authToken.getId());
        return new Response<>(new BodyLogSliceRes(bodyLogSlice));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping("/{bodyLogId}")
    public Response<?> updateById(
            @Auth AuthToken authToken,
            @PathVariable("bodyLogId") Long bodyLogId,
            @RequestBody @Valid UpdateBodyLogReq req
    ) throws BodyLogNotFoundException {
        bodyLogService.updateById(bodyLogId, authToken.getId(), req.toEditor());
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{bodyLogId}")
    public Response<?> deleteById(@Auth AuthToken authToken, @PathVariable("bodyLogId") Long bodyLogId) {
        bodyLogService.deleteById(bodyLogId, authToken.getId());
        return new Response<>();
    }
}
