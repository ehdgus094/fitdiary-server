package im.fitdiary.fitdiaryserver.exercise.presentation;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.CreateExerciseLogDetailListReq;
import im.fitdiary.fitdiaryserver.exercise.presentation.dto.CreateExerciseLogDetailReq;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseLogDetailService;
import im.fitdiary.fitdiaryserver.security.argumentresolver.Auth;
import im.fitdiary.fitdiaryserver.security.argumentresolver.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/exercise/log/detail")
@RestController
public class ExerciseLogDetailController {

    private final ExerciseLogDetailService exerciseLogDetailService;

    @Secured("ROLE_USER_ACCESS")
    @PostMapping
    public Response create(
            @Auth AuthToken authToken,
            @RequestBody @Valid CreateExerciseLogDetailListReq req
    ) {
        exerciseLogDetailService.create(
                req.getExerciseLogId(),
                authToken.getId(),
                req.getData().stream()
                        .map(CreateExerciseLogDetailReq::toDto)
                        .collect(Collectors.toList())
        );
        return Response.success();
    }
}
