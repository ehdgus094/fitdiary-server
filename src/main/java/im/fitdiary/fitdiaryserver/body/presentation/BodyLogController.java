package im.fitdiary.fitdiaryserver.body.presentation;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.body.presentation.dto.BodyLogRes;
import im.fitdiary.fitdiaryserver.body.presentation.dto.BodyLogSliceRes;
import im.fitdiary.fitdiaryserver.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.service.BodyLogService;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/body/log")
@RestController
public class BodyLogController {

    private final BodyLogService bodyLogService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response create(@Auth AuthToken authToken, @RequestBody @Valid CreateBodyLogReq req) {
        BodyLog bodyLog = bodyLogService.create(req.toServiceDto(authToken.getId()));
        return Response.success(new BodyLogRes(bodyLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/search-latest")
    public Response searchLatest(@Auth AuthToken authToken, Pageable pageable) {
        BodyLogSlice bodyLogSlice = bodyLogService.searchLatest(pageable, authToken.getId());
        return Response.success(new BodyLogSliceRes(bodyLogSlice));
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{bodyLogId}")
    public Response delete(@Auth AuthToken authToken, @PathVariable("bodyLogId") Long bodyLogId) {
        bodyLogService.deleteById(bodyLogId, authToken.getId());
        return Response.success();
    }
}