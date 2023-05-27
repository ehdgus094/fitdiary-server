package im.fitdiary.fitdiaryserver.exercise.presentation;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.*;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseLogService;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/exercise/log")
@RestController
public class ExerciseLogController {

    private final ExerciseLogService exerciseLogService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response create(@Auth AuthToken authToken, @RequestBody @Valid CreateExerciseLogReq req) {
        ExerciseLog exerciseLog = exerciseLogService.create(req.toDto(authToken.getId()));
        return Response.success(new ExerciseLogRes(exerciseLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @PostMapping("/detail")
    public Response createDetailsBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid CreateExerciseLogDetailListReq req
    ) {
        exerciseLogService.bulkInsertDetail(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .map(CreateExerciseLogDetailReq::toDto)
                        .collect(Collectors.toList())
        );
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/{exerciseLogId}")
    public Response findById(@Auth AuthToken authToken, @PathVariable("exerciseLogId") Long exerciseLogId) {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, authToken.getId());
        return Response.success(new ExerciseLogRes(exerciseLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/detail/{exerciseLogDetailId}")
    public Response findDetailById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) {
        ExerciseLogDetail detail = exerciseLogService.findDetailById(exerciseLogDetailId, authToken.getId());
        return Response.success(new ExerciseLogDetailRes(detail));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping("/detail")
    public Response updateDetailsBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid UpdateExerciseLogDetailListReq req
    ) {
        exerciseLogService.bulkUpdateDetail(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .collect(Collectors.toMap(
                                UpdateExerciseLogDetailReq::getExerciseLogDetailId,
                                UpdateExerciseLogDetailReq::toEditor)
                        )
        );
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseLogId}")
    public Response deleteById(@Auth AuthToken authToken, @PathVariable("exerciseLogId") Long exerciseLogId) {
        exerciseLogService.deleteById(exerciseLogId, authToken.getId());
        return Response.success();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/detail/{exerciseLogDetailId}")
    public Response deleteDetailById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) {
        exerciseLogService.deleteDetailById(exerciseLogDetailId, authToken.getId());
        return Response.success();
    }
}
