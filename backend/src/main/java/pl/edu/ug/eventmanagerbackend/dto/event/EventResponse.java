package pl.edu.ug.eventmanagerbackend.dto.event;

import pl.edu.ug.eventmanagerbackend.domain.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        UUID userId,
        String title,
        String location,
        LocalDate date,
        LocalTime time,
        String description
) {

    public static EventResponse fromEntity(Event event) {
        return new EventResponse(
                event.getId(),
                event.getUser().getId(),
                event.getTitle(),
                event.getLocation(),
                event.getDate(),
                event.getTime(),
                event.getDescription()
        );
    }

}
