package pl.edu.ug.eventmanagerbackend.dto.user;

import pl.edu.ug.eventmanagerbackend.domain.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail()
        );
    }

}
