package pl.edu.ug.eventmanagerbackend.dto.user;

public record UserRequest(
        String email,
        String password
) {}
