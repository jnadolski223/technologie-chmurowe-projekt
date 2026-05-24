package pl.edu.ug.eventmanagerbackend.dto.user;

import java.util.UUID;

public record UserLoginResponse(
        UUID userId
) {}
