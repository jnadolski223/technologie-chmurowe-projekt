package pl.edu.ug.eventmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.ug.eventmanagerbackend.config.ApiPaths;
import pl.edu.ug.eventmanagerbackend.dto.ApiResponse;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingCreateRequest;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingResponse;
import pl.edu.ug.eventmanagerbackend.service.BookingService;

import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.BOOKINGS)
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBooking(@RequestBody BookingCreateRequest request) {
        bookingService.createBooking(request);

        return ResponseEntity.accepted().body(ApiResponse.success(
                HttpStatus.ACCEPTED,
                "Booking creation request accepted",
                ApiPaths.BOOKINGS,
                null
        ));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable UUID bookingId) {
        BookingResponse response = bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Booking fetched successfully",
                ApiPaths.EVENTS + "/" + bookingId,
                response
        ));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);

        return ResponseEntity.accepted().body(ApiResponse.success(
                HttpStatus.ACCEPTED,
                "Booking deletion request accepted",
                ApiPaths.BOOKINGS,
                null
        ));
    }

}
