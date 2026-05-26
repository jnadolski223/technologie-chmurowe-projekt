package pl.edu.ug.eventmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ug.eventmanagerbackend.domain.User;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse registerUser(UserRequest request) {
        validateUserRequest(request);

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Given email is already taken");
        }

        User user = User.builder()
                .email(request.email())
                .password(request.password())
                .build();

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public UserLoginResponse loginUser(UserRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.getPassword().equals(request.password())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return UserLoginResponse.fromEntity(user);
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

        if (!request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new ConflictException("Given email is already taken");
            } else {
                user.setEmail(request.email());
            }
        }

        if (!request.password().equals(user.getPassword())) {
            user.setPassword(request.password());
        }

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        userRepository.delete(user);
    }

    private void validateUserRequest(UserRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new UnprocessableContentException("Field 'email' cannot be empty");
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new UnprocessableContentException("Field 'password' cannot be empty");
        }
    }

}
