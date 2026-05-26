package pl.edu.ug.eventmanagerbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.ug.eventmanagerbackend.dto.ApiResponseWrapper;
import pl.edu.ug.eventmanagerbackend.exception.ConflictException;
import pl.edu.ug.eventmanagerbackend.exception.NotFoundException;
import pl.edu.ug.eventmanagerbackend.exception.UnauthorizedException;
import pl.edu.ug.eventmanagerbackend.exception.UnprocessableContentException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 401 Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleUnauthorized(
            UnauthorizedException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseWrapper.error(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request.getRequestURL().toString()
        ));
    }

    // 404 Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleNotFound(
            NotFoundException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.error(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURL().toString()
        ));
    }

    // 409 Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleConflict(
            ConflictException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseWrapper.error(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURL().toString()
        ));
    }

    // 422 Unprocessable Content
    @ExceptionHandler(UnprocessableContentException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleUnprocessableContent(
            UnprocessableContentException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ApiResponseWrapper.error(
                HttpStatus.UNPROCESSABLE_CONTENT,
                exception.getMessage(),
                request.getRequestURL().toString()
        ));
    }

    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleException(
            Exception exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseWrapper.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                request.getRequestURL().toString()
        ));
    }

}
