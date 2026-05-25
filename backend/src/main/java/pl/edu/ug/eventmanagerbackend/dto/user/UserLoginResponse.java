package pl.edu.ug.eventmanagerbackend.dto.user;

import pl.edu.ug.eventmanagerbackend.domain.User;

import java.util.UUID;

public record UserLoginResponse(
        UUID id
) {

    public static UserLoginResponse fromEntity(User user) {
        return new UserLoginResponse(
                user.getId()
        );
    }

}
