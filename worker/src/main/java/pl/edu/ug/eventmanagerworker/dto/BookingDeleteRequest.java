package pl.edu.ug.eventmanagerworker.dto;

import java.util.UUID;

public record BookingDeleteRequest(
        UUID bookingId
) {}
