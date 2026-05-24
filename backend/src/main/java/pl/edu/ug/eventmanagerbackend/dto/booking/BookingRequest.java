package pl.edu.ug.eventmanagerbackend.dto.booking;

import java.util.UUID;

public record BookingRequest(
        UUID userId,
        UUID eventId
) {}
