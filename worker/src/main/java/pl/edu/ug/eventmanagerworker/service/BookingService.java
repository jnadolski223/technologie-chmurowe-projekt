package pl.edu.ug.eventmanagerworker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ug.eventmanagerworker.domain.Booking;
import pl.edu.ug.eventmanagerworker.domain.Event;
import pl.edu.ug.eventmanagerworker.domain.User;
import pl.edu.ug.eventmanagerworker.dto.BookingCreateRequest;
import pl.edu.ug.eventmanagerworker.dto.BookingDeleteRequest;
import pl.edu.ug.eventmanagerworker.repository.BookingRepository;
import pl.edu.ug.eventmanagerworker.repository.EventRepository;
import pl.edu.ug.eventmanagerworker.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public void createBooking(BookingCreateRequest request) {
        Optional<User> userOptional = userRepository.findById(request.userId());
        if (userOptional.isEmpty()) {
            log.error("Booking creation cancelled: User with ID {} not found", request.userId());
            return;
        }

        Optional<Event> eventOptional = eventRepository.findById(request.eventId());
        if (eventOptional.isEmpty()) {
            log.error("Booking creation cancelled: Event with ID {} not found", request.eventId());
            return;
        }

        User user = userOptional.get();
        Event event = eventOptional.get();

        if (event.getUser().getId().equals(user.getId())) {
            log.error("Booking creation cancelled: User ({}) cannot be booked for its own event ({})", user.getId(), event.getId());
            return;
        }

        if (bookingRepository.existsByUserAndEvent(user, event)) {
            log.error("User ({}) is already booked for the event ({})", user.getId(), event.getId());
            return;
        }

        Booking booking = Booking.builder()
                .user(user)
                .event(event)
                .bookingTime(Instant.now())
                .build();

        bookingRepository.save(booking);

        log.info("Booking creation succeed! (userId: {}, eventId: {})", booking.getUser().getId(), booking.getEvent().getId());
    }

    public void deleteBooking(BookingDeleteRequest request) {
        Optional<Booking> bookingOptional = bookingRepository.findById(request.bookingId());
        if (bookingOptional.isEmpty()) {
            log.error("Booking deletion cancelled: Booking with ID {} not found", request.bookingId());
            return;
        }

        Booking booking = bookingOptional.get();

        bookingRepository.delete(booking);

        log.info("Booking deletion succeed! (bookingId: {})", booking.getId());
    }

}
