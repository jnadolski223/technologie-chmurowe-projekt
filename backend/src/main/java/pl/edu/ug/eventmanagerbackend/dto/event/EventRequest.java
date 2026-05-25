package pl.edu.ug.eventmanagerbackend.dto.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record EventRequest(
        UUID userId,
        String title,
        String location,
        LocalDate date,
        LocalTime time,
        String description
) {}
