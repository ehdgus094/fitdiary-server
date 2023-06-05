package im.fitdiary.server.exception.advice;

import im.fitdiary.server.exception.dto.FailureResponse;
import im.fitdiary.server.exception.e401.BaseUnauthorizedException;
import im.fitdiary.server.exception.e404.NotFoundException;
import im.fitdiary.server.exception.e409.ConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private static final String BASE_LOG_MESSAGE = "response failure: {}";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public FailureResponse exception(Exception e) {
        log.error("", e);
        String message =
                "[배포모드에서는 에러 메시지가 생략됩니다] " +
                e.getMessage();
        return new FailureResponse(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public FailureResponse accessDenied(AccessDeniedException e) {
        log.info(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse("unauthorized");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public FailureResponse missingRequestHeader(MissingRequestHeaderException e) {
        log.warn(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse("unauthorized");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureResponse constraintViolation(ConstraintViolationException e) {
        // Bean Validation
        log.warn(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureResponse methodArgumentNotValid(MethodArgumentNotValidException e) {
        // Bean Validation
        final String SEPARATOR = " | ";
        StringBuilder builder = new StringBuilder();

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError error : allErrors) {
            String message = Arrays.stream(Objects.requireNonNull(error.getCodes()))
                    .map(code -> {
                        Object[] arguments = error.getArguments();
                        Locale locale = LocaleContextHolder.getLocale();
                        try {
                            return messageSource.getMessage(code, arguments, locale);
                        } catch (NoSuchMessageException ex) {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElse(error.getDefaultMessage());
            builder.append(message);
            builder.append(SEPARATOR);
        }
        builder.delete(builder.lastIndexOf(SEPARATOR), builder.length());
        log.warn(BASE_LOG_MESSAGE, builder);
        builder.insert(0, "[배포모드에서는 에러 메시지가 생략됩니다] ");
        return new FailureResponse(builder.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureResponse httpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureResponse httpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.warn(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureResponse httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureResponse methodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(BaseUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public FailureResponse unauthorized(BaseUnauthorizedException e) {
        log.info(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FailureResponse notFound(NotFoundException e) {
        log.info(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public FailureResponse conflict(ConflictException e) {
        log.info(BASE_LOG_MESSAGE, e.getMessage());
        return new FailureResponse(e.getMessage());
    }
}
