package im.fitdiary.server.exercise.presentation;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.dto.Response;
import im.fitdiary.server.exception.e404.ExerciseLogNotFoundException;
import im.fitdiary.server.exercise.data.entity.ExerciseLog;
import im.fitdiary.server.exercise.presentation.dto.*;
import im.fitdiary.server.exercise.service.ExerciseLogService;
import im.fitdiary.server.security.argumentresolver.Auth;
import im.fitdiary.server.security.argumentresolver.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Exercise - Log")
@BaseMethodLogging
@RequiredArgsConstructor
@RequestMapping("/exercise/logs")
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
    @GetMapping("/{exerciseLogId}")
    public Response<ExerciseLogRes> findById(
            @Auth AuthToken authToken,
            @PathVariable("exerciseLogId") Long exerciseLogId
    ) throws ExerciseLogNotFoundException {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, authToken.getId());
        return new Response<>(new ExerciseLogRes(exerciseLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseLogId}")
    public Response<?> deleteById(@Auth AuthToken authToken, @PathVariable("exerciseLogId") Long exerciseLogId) {
        exerciseLogService.deleteById(exerciseLogId, authToken.getId());
        return new Response<>();
    }
}
