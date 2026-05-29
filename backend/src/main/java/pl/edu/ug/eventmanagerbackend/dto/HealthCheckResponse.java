package pl.edu.ug.eventmanagerbackend.dto;

public record HealthCheckResponse(
        String status,
        String message,
        String version
) {}
