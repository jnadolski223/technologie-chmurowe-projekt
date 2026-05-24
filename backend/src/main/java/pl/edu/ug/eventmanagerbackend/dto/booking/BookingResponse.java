package pl.edu.ug.eventmanagerbackend.dto.booking;

import pl.edu.ug.eventmanagerbackend.domain.Booking;

import java.time.Instant;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID userId,
        UUID eventId,
        Instant bookingTime
) {

    public static BookingResponse fromEntity(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getEvent().getId(),
                booking.getBookingTime()
        );
    }

}
