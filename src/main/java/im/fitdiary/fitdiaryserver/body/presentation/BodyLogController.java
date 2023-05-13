package im.fitdiary.fitdiaryserver.body.presentation;

import im.fitdiary.fitdiaryserver.body.presentation.dto.BodyLogSliceRes;
import im.fitdiary.fitdiaryserver.body.presentation.dto.CreateBodyLogReq;
import im.fitdiary.fitdiaryserver.body.service.BodyLogService;
import im.fitdiary.fitdiaryserver.body.service.dto.BodyLogSlice;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.security.argumentresolver.UserId;
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
    public Response create(@UserId Long userId, @RequestBody @Valid CreateBodyLogReq req) {
        bodyLogService.create(req.toServiceDto(userId));
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/search-latest")
    public Response searchLatest(@UserId Long userId, Pageable pageable) {
        BodyLogSlice bodyLogSlice = bodyLogService.searchLatest(pageable, userId);
        return Response.success(new BodyLogSliceRes(bodyLogSlice));
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{bodyLogId}")
    public Response delete(@UserId Long userId, @PathVariable("bodyLogId") Long bodyLogId) {
        bodyLogService.deleteById(bodyLogId, userId);
        return Response.success();
    }
}
