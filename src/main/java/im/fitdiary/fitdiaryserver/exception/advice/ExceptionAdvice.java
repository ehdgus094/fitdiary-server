package im.fitdiary.fitdiaryserver.exception.advice;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.config.properties.Mode;
import im.fitdiary.fitdiaryserver.exception.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.NotFoundException;
import im.fitdiary.fitdiaryserver.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ConfigProperties properties;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.error(e.toString());
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response unauthorized() {
        return Response.failure("unauthorized");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notFound(NotFoundException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValid(MethodArgumentNotValidException e) {
        if (properties.getMode().equals(Mode.PROD)) return Response.failure("bad request");

        final String SEPARATOR = " | ";
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
            builder.append(SEPARATOR);
        }
        builder.delete(builder.lastIndexOf(SEPARATOR), builder.length());
        return Response.failure(builder.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response httpMessageNotReadable(HttpMessageNotReadableException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response httpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response constraintViolation(ConstraintViolationException e) {
        // db 제약조건 에러
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(InvalidLoginInfoException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response invalidLoginInfo(InvalidLoginInfoException e) {
        return Response.failure(e.getMessage());
    }
}
