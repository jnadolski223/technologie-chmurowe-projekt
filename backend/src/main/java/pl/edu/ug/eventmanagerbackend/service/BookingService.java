package pl.edu.ug.eventmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ug.eventmanagerbackend.config.RabbitMQProducerConfig;
import pl.edu.ug.eventmanagerbackend.domain.Event;
import pl.edu.ug.eventmanagerbackend.domain.User;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingCreateRequest;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingDeleteRequest;
import pl.edu.ug.eventmanagerbackend.dto.booking.BookingResponse;
import pl.edu.ug.eventmanagerbackend.exception.NotFoundException;
import pl.edu.ug.eventmanagerbackend.repository.BookingRepository;
import pl.edu.ug.eventmanagerbackend.repository.EventRepository;
import pl.edu.ug.eventmanagerbackend.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final RabbitTemplate rabbitTemplate;

    public void createBooking(BookingCreateRequest request) {
        rabbitTemplate.convertAndSend(
                RabbitMQProducerConfig.BOOKING_EXCHANGE,
                RabbitMQProducerConfig.BOOKING_CREATE_ROUTING_KEY,
                request
        );
    }

    public BookingResponse getBookingById(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .map(BookingResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + bookingId + " not found"));
    }

    public List<BookingResponse> getAllBookingsByUserId(UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        return user
                .getBookings()
                .stream()
                .map(BookingResponse::fromEntity)
                .toList();
    }

    public List<BookingResponse> getAllBookingsByEventId(UUID eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID " + eventId + " not found"));

        return event
                .getBookings()
                .stream()
                .map(BookingResponse::fromEntity)
                .toList();
    }

    public void deleteBooking(UUID bookingId) {
        BookingDeleteRequest request = new BookingDeleteRequest(bookingId);

        rabbitTemplate.convertAndSend(
                RabbitMQProducerConfig.BOOKING_EXCHANGE,
                RabbitMQProducerConfig.BOOKING_DELETE_ROUTING_KEY,
                request
        );
    }

}
