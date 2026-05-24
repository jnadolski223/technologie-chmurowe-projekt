package pl.edu.ug.eventmanagerbackend.dto.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventRequest(
        UUID userId,
        String title,
        LocalDateTime dateAndTime,
        String location,
        String description
) {}
