package pl.edu.ug.eventmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.ug.eventmanagerbackend.config.ApiPaths;
import pl.edu.ug.eventmanagerbackend.dto.ApiResponseWrapper;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingResponse;
import pl.edu.ug.eventmanagerbackend.dto.event.EventResponse;
import pl.edu.ug.eventmanagerbackend.dto.user.UserLoginResponse;
import pl.edu.ug.eventmanagerbackend.dto.user.UserRequest;
import pl.edu.ug.eventmanagerbackend.dto.user.UserResponse;
import pl.edu.ug.eventmanagerbackend.service.BookingService;
import pl.edu.ug.eventmanagerbackend.service.EventService;
import pl.edu.ug.eventmanagerbackend.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final EventService eventService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserResponse>> registerUser(@RequestBody UserRequest request) {
        UserResponse response = userService.registerUser(request);
        URI location = URI.create(ApiPaths.USERS + "/" + response.id());

        return ResponseEntity.created(location).body(ApiResponseWrapper.success(
                HttpStatus.CREATED,
                "User registered successfully",
                ApiPaths.USERS + "/register",
                response
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<UserLoginResponse>> loginUser(@RequestBody UserRequest credentials) {
        UserLoginResponse response = userService.loginUser(credentials);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "User logged in successfully",
                ApiPaths.USERS + "/login",
                response
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseWrapper<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse response = userService.getUserById(userId);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "User fetched successfully",
                ApiPaths.USERS + "/" + userId,
                response
        ));
    }

    @GetMapping("/{userId}/bookings")
    public ResponseEntity<ApiResponseWrapper<List<BookingResponse>>> getAllBookingByUserId(@PathVariable UUID userId) {
        List<BookingResponse> response = bookingService.getAllBookingsByUserId(userId);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "User's bookings fetched successfully",
                ApiPaths.USERS + "/" + userId + "/bookings",
                response
        ));
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<ApiResponseWrapper<List<EventResponse>>> getAllEventsByUserId(@PathVariable UUID userId) {
        List<EventResponse> response = eventService.getAllEventsByUserId(userId);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "User's events fetched successfully",
                ApiPaths.USERS + "/" + userId + "/events",
                response
        ));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseWrapper<UserResponse>> updateUser(@PathVariable UUID userId, @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(userId, request);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "User updated successfully",
                ApiPaths.USERS + "/" + userId,
                response
        ));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }

}
