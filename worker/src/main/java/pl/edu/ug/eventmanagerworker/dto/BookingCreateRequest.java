package pl.edu.ug.eventmanagerworker.dto;

import java.util.UUID;

public record BookingCreateRequest(
        UUID userId,
        UUID eventId
) {}
