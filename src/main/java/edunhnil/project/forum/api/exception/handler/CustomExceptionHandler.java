package edunhnil.project.forum.api.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.exception.BadSqlException;
import edunhnil.project.forum.api.exception.InvalidDateFormat;
import edunhnil.project.forum.api.exception.InvalidPasswordException;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.NotEncodePasswordException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.exception.UnauthorizedException;
import edunhnil.project.forum.api.log.AppLogger;
import edunhnil.project.forum.api.log.LoggerFactory;
import edunhnil.project.forum.api.log.LoggerType;
import io.jsonwebtoken.SignatureException;

;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

    @ExceptionHandler(InvalidDateFormat.class)
    public ResponseEntity<CommonResponse<String>> handleInvalidDateFormatException(InvalidDateFormat e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<CommonResponse<String>> handleInvalidRequestException(InvalidRequestException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(NotEncodePasswordException.class)
    public ResponseEntity<CommonResponse<String>> handleNotEncodePasswordException(NotEncodePasswordException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<CommonResponse<String>> handleInvalidPasswordException(InvalidPasswordException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(BadSqlException.class)
    public ResponseEntity<CommonResponse<String>> handleBadSqlException(BadSqlException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CommonResponse<String>> handleUnAuthorizedException(UnauthorizedException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.UNAUTHORIZED.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<String>> handleForbidden(AuthenticationException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(false, null, "Access denied!", HttpStatus.FORBIDDEN.value()), null,
                HttpStatus.OK.value());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<CommonResponse<String>> handleSignatureException(SignatureException e) {
        APP_LOGGER.error(e.getMessage());
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(false, null, e.getMessage(), HttpStatus.UNAUTHORIZED.value()), null,
                HttpStatus.OK.value());
    }

}
