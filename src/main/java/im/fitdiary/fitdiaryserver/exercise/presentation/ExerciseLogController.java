package im.fitdiary.fitdiaryserver.exercise.presentation;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.exercise.data.entity.ExerciseLog;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.CreateExerciseLogReq;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.ExerciseLogRes;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseLogService;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @GetMapping("/{exerciseLogId}")
    public Response findById(@Auth AuthToken authToken, @PathVariable("exerciseLogId") Long exerciseLogId) {
        ExerciseLog exerciseLog = exerciseLogService.findById(exerciseLogId, authToken.getId());
        return Response.success(new ExerciseLogRes(exerciseLog));
    }

    @Secured("ROLE_USER_ACCESS")
    @DeleteMapping("/{exerciseLogId}")
    public Response deleteById(@Auth AuthToken authToken, @PathVariable("exerciseLogId") Long exerciseLogId) {
        exerciseLogService.deleteById(exerciseLogId, authToken.getId());
        return Response.success();
    }
}
