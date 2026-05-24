package pl.edu.ug.eventmanagerbackend.dto.user;

public record UserLoginRequest(
        String email,
        String password
) {}
