package im.fitdiary.fitdiaryserver.exception.advice;

import im.fitdiary.fitdiaryserver.common.dto.Response;
import im.fitdiary.fitdiaryserver.exception.e401.BaseUnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.NotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.ConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.error(e.toString());
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response accessDenied() {
        return Response.failure("unauthorized");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response missingRequestHeader() {
        return Response.failure("unauthorized");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValid(MethodArgumentNotValidException e) {
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

    @ExceptionHandler(BaseUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response unauthorized(BaseUnauthorizedException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notFound(NotFoundException e) {
        return Response.failure(e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicated(ConflictException e) {
        return Response.failure(e.getMessage());
    }
}
