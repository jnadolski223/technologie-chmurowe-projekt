package pl.edu.ug.eventmanagerbackend.dto.event;

import pl.edu.ug.eventmanagerbackend.domain.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        UUID userId,
        String title,
        LocalDateTime dateAndTime,
        String location,
        String description
) {

    public static EventResponse fromEntity(Event event) {
        return new EventResponse(
                event.getId(),
                event.getUser().getId(),
                event.getTitle(),
                event.getDateAndTime(),
                event.getLocation(),
                event.getDescription()
        );
    }

}
