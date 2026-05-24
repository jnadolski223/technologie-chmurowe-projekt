package pl.edu.ug.eventmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ug.eventmanagerbackend.domain.User;
import pl.edu.ug.eventmanagerbackend.dto.user.UserLoginRequest;
import pl.edu.ug.eventmanagerbackend.dto.user.UserLoginResponse;
import pl.edu.ug.eventmanagerbackend.dto.user.UserRequest;
import pl.edu.ug.eventmanagerbackend.dto.user.UserResponse;
import pl.edu.ug.eventmanagerbackend.exception.ConflictException;
import pl.edu.ug.eventmanagerbackend.exception.NotFoundException;
import pl.edu.ug.eventmanagerbackend.exception.UnauthorizedException;
import pl.edu.ug.eventmanagerbackend.exception.UnprocessableContentException;
import pl.edu.ug.eventmanagerbackend.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse registerUser(UserRequest request) {
        if (request.name() == null) {
            throw new UnprocessableContentException("Field 'name' cannot be empty");
        }

        if (request.email() == null) {
            throw new UnprocessableContentException("Field 'email' cannot be empty");
        }

        if (request.password() == null) {
            throw new UnprocessableContentException("Field 'password' cannot be empty");
        }

        if (userRepository.existsByName(request.name())) {
            throw new ConflictException("Given name is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Given email is already taken");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public UserLoginResponse loginUser(UserLoginRequest credentials) {
        User user = userRepository
                .findByEmail(credentials.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.getPassword().equals(credentials.password())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return new UserLoginResponse(user.getId());
    }

    public UserResponse getUserById(UUID userId) {
        return userRepository
                .findById(userId)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UserRequest request) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        updateName(request.name(), user);
        updateEmail(request.email(), user);
        updatePassword(request.password(), user);

        return null;
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        userRepository.delete(user);
    }

    private void updateName(String name, User user) {
        if (name == null) {
            throw new UnprocessableContentException("Field 'name' cannot be empty");
        }

        if (name.equals(user.getName())) {
            return;
        }

        boolean isNameTaken = userRepository.existsByName(name);
        if (isNameTaken) {
            throw new ConflictException("Given name is already taken");
        }

        user.setName(name);
    }

    private void updateEmail(String email, User user) {
        if (email == null) {
            throw new UnprocessableContentException("Field 'email' cannot be empty");
        }

        if (email.equals(user.getEmail())) {
            return;
        }

        boolean isEmailTaken = userRepository.existsByEmail(email);
        if (isEmailTaken) {
            throw new ConflictException("Given email is already taken");
        }

        user.setEmail(email);
    }

    private void updatePassword(String password, User user) {
        if (password == null) {
            throw new UnprocessableContentException("Field 'password' cannot be empty");
        }

        if (password.equals(user.getPassword())) {
            return;
        }

        user.setPassword(password);
    }

}
