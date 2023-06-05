package im.fitdiary.server.exercise.presentation;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e404.ExerciseLogDetailNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exception.e404.ExerciseNotFoundException;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.data.entity.ExerciseLogDetail;
import im.fitdiary.server.exercise.presentation.dto.*;
import im.fitdiary.server.exercise.service.ExerciseLogService;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Tag(name = "ExerciseLog")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/exercise/log")
@RestController
public class ExerciseLogController {

    private final ExerciseLogService exerciseLogService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response<ExerciseLogRes> create(@Auth AuthToken authToken, @RequestBody @Valid CreateExerciseLogReq req) {
        ExerciseLog exerciseLog = exerciseLogService.create(req.toDto(authToken.getId()));
        return new Response<>(new ExerciseLogRes(exerciseLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @PostMapping("/detail")
    public Response<?> createDetailsBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid CreateExerciseLogDetailListReq req
    ) throws ExerciseLogNotFoundException, ExerciseNotFoundException {
        exerciseLogService.bulkInsertDetail(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .map(CreateExerciseLogDetailReq::toDto)
                        .collect(Collectors.toList())
        );
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/{exerciseLogId}")
    public Response<ExerciseLogRes> findById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogId") Long exerciseLogId
    ) throws ExerciseLogNotFoundException {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, authToken.getId());
        return new Response<>(new ExerciseLogRes(exerciseLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @GetMapping("/detail/{exerciseLogDetailId}")
    public Response<ExerciseLogDetailRes> findDetailById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) throws ExerciseLogDetailNotFoundException {
        ExerciseLogDetail detail = exerciseLogService.findDetailById(exerciseLogDetailId, authToken.getId());
        return new Response<>(new ExerciseLogDetailRes(detail));
    }

    @Secured("ROLE_USER_ACCESS")
    @PutMapping("/detail")
    public Response<?> updateDetailsBulk(
            @Auth AuthToken authToken,
            @RequestBody @Valid UpdateExerciseLogDetailListReq req
    ) throws ExerciseLogNotFoundException {
        exerciseLogService.bulkUpdateDetail(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .collect(Collectors.toMap(
                                UpdateExerciseLogDetailReq::getExerciseLogDetailId,
                                UpdateExerciseLogDetailReq::toEditor)
                        )
        );
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseLogId}")
    public Response<?> deleteById(@Auth AuthToken authToken, @PathVariable("exerciseLogId") Long exerciseLogId) {
        exerciseLogService.deleteById(exerciseLogId, authToken.getId());
        return new Response<>();
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/detail/{exerciseLogDetailId}")
    public Response<?> deleteDetailById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogDetailId") Long exerciseLogDetailId
    ) {
        exerciseLogService.deleteDetailById(exerciseLogDetailId, authToken.getId());
        return new Response<>();
    }
}
