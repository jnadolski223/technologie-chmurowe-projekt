package pl.edu.ug.eventmanagerbackend.dto.user;

public record UserRequest(
        String name,
        String email,
        String password
) {}
