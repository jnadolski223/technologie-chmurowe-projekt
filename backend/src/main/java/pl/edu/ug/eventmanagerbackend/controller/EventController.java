package pl.edu.ug.eventmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.ug.eventmanagerbackend.config.ApiPaths;
import pl.edu.ug.eventmanagerbackend.dto.ApiResponseWrapper;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingResponse;
import pl.edu.ug.eventmanagerbackend.dto.event.EventRequest;
import pl.edu.ug.eventmanagerbackend.dto.event.EventResponse;
import pl.edu.ug.eventmanagerbackend.service.BookingService;
import pl.edu.ug.eventmanagerbackend.service.EventService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.EVENTS)
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<EventResponse>> createEvent(@RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        URI location = URI.create(ApiPaths.EVENTS + "/" + response.id());

        return ResponseEntity.created(location).body(ApiResponseWrapper.success(
                HttpStatus.CREATED,
                "Event created successfully",
                ApiPaths.EVENTS,
                response
        ));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponseWrapper<EventResponse>> getEventById(@PathVariable UUID eventId) {
        EventResponse response = eventService.getEventById(eventId);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "Event fetched successfully",
                ApiPaths.EVENTS + "/" + eventId,
                response
        ));
    }

    @GetMapping("/{eventId}/bookings")
    public ResponseEntity<ApiResponseWrapper<List<BookingResponse>>> getAllBookingsByEventId(@PathVariable UUID eventId) {
        List<BookingResponse> response = bookingService.getAllBookingsByEventId(eventId);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "Event's bookings fetched successfully",
                ApiPaths.EVENTS + "/" + eventId + "/bookings",
                response
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<EventResponse>>> getAllEvents() {
        List<EventResponse> response = eventService.getAllEvents();

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "List of events fetched successfully",
                ApiPaths.EVENTS,
                response
        ));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponseWrapper<EventResponse>> updateEvent(@PathVariable UUID eventId, @RequestBody EventRequest request) {
        EventResponse response = eventService.updateEvent(eventId, request);

        return ResponseEntity.ok(ApiResponseWrapper.success(
                HttpStatus.OK,
                "Event updated successfully",
                ApiPaths.EVENTS + "/" + eventId,
                response
        ));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);

        return ResponseEntity.noContent().build();
    }

}
