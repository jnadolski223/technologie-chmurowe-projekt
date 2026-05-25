package pl.edu.ug.eventmanagerbackend.dto.booking;

import java.util.UUID;

public record BookingCreateRequest(
        UUID userId,
        UUID eventId
) {}
