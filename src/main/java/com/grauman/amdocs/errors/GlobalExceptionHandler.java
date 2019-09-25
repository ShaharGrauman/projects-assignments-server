package com.grauman.amdocs.errors;

import com.grauman.amdocs.errors.custom.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({SQLException.class, NullPointerException.class, IndexOutOfBoundsException.class})
    protected ResponseEntity<GeneralError> handleInternalErrors(RuntimeException ex, WebRequest request) {
        return generateError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler({ResultsNotFoundException.class})
    protected ResponseEntity<GeneralError> handleNotFound(RuntimeException ex, WebRequest request) {
        return generateError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({InvalidDataException.class, InvalidCredentials.class})
    protected ResponseEntity<GeneralError> handleBadRequest(RuntimeException ex, WebRequest request) {
        return generateError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    private ResponseEntity<GeneralError> generateError(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new GeneralError(status, message));
    }

    @ExceptionHandler({LevelValidityException.class, NotContentExistException.class, AlreadyExistsException.class,ValidationsCheckException.class})
    protected ResponseEntity<GeneralError> handleRangeErrors(RuntimeException ex, WebRequest request) {
        return generateError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    //searchEmployeeProfile who is deactivated..return 204 no contant
//    @ExceptionHandler({NotContentExistException.class})
//    protected ResponseEntity<GeneralError> handleEmployeeNotFound(RuntimeException ex, WebRequest request) {
//        return generateError(HttpStatus.NO_CONTENT, ex.getMessage());
//    }


    /**
     * can handle missing or invalid arguments
     * uncomment if needed
     */
    /*@Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        return generateError(HttpStatus.BAD_REQUEST, ex.getMessage())
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        return generateError(HttpStatus.BAD_REQUEST, ex.getMessage())
    }
     */
}
