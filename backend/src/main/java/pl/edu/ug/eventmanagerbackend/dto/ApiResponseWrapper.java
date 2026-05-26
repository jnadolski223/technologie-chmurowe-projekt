package pl.edu.ug.eventmanagerbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseWrapper<T>(
        Instant timestamp,
        int code,
        String status,
        String message,
        String path,
        T data
) {

    public static <T> ApiResponseWrapper<T> success(HttpStatus httpStatus, String message, String path, T data) {
        return new ApiResponseWrapper<>(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path,
                data
        );
    }

    public static <T> ApiResponseWrapper<T> error(HttpStatus httpStatus, String message, String path) {
        return new ApiResponseWrapper<>(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path,
                null
        );
    }

}
