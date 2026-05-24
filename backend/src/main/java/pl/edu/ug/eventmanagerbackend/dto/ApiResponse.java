package pl.edu.ug.eventmanagerbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        LocalDateTime timestamp,
        int code,
        String status,
        String message,
        String path,
        T data
) {

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, String path, T data) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path,
                data
        );
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message, String path) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path,
                null
        );
    }

}