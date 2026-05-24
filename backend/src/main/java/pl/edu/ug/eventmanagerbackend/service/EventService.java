package pl.edu.ug.eventmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ug.eventmanagerbackend.domain.Event;
import pl.edu.ug.eventmanagerbackend.domain.User;
import pl.edu.ug.eventmanagerbackend.dto.event.EventRequest;
import pl.edu.ug.eventmanagerbackend.dto.event.EventResponse;
import pl.edu.ug.eventmanagerbackend.exception.NotFoundException;
import pl.edu.ug.eventmanagerbackend.exception.UnprocessableContentException;
import pl.edu.ug.eventmanagerbackend.repository.EventRepository;
import pl.edu.ug.eventmanagerbackend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        if (request.userId() == null) {
            throw new UnprocessableContentException("Field 'userId' cannot be empty");
        }

        if (request.title() == null) {
            throw new UnprocessableContentException("Field 'title' cannot be empty");
        }

        if (request.dateAndTime() == null) {
            throw new UnprocessableContentException("Field 'dateAndTime' cannot be empty");
        }

        if (request.location() == null) {
            throw new UnprocessableContentException("Field 'location' cannot be empty");
        }

        if (request.description() == null) {
            throw new UnprocessableContentException("Field 'description' cannot be empty");
        }

        User user = userRepository
                .findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User with ID " + request.userId() + " not found"));

        Event event = Event.builder()
                .user(user)
                .title(request.title())
                .dateAndTime(request.dateAndTime())
                .location(request.location())
                .description(request.description())
                .build();

        return EventResponse.fromEntity(eventRepository.save(event));
    }

    public EventResponse getEventById(UUID eventId) {
        return eventRepository
                .findById(eventId)
                .map(EventResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Event with ID " + eventId + " not found"));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository
                .findAll()
                .stream()
                .map(EventResponse::fromEntity)
                .toList();
    }

    public List<EventResponse> getAllEventsByUserId(UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        return user
                .getEvents()
                .stream()
                .map(EventResponse::fromEntity)
                .toList();
    }

    @Transactional
    public EventResponse updateEvent(UUID eventId, EventRequest request) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID " + eventId + " not found"));

        updateTitle(request.title(), event);
        updateDateAndTime(request.dateAndTime(), event);
        updateLocation(request.location(), event);
        updateDescription(request.description(), event);

        return null;
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID " + eventId + " not found"));

        eventRepository.delete(event);
    }

    private void updateTitle(String title, Event event) {
        if (title == null) {
            throw new UnprocessableContentException("Field 'title' cannot be empty");
        }

        if (title.equals(event.getTitle())) {
            return;
        }

        event.setTitle(title);
    }

    private void updateDateAndTime(LocalDateTime dateAndTime, Event event) {
        if (dateAndTime == null) {
            throw new UnprocessableContentException("Field 'dateAndTime' cannot be empty");
        }

        if (dateAndTime.isEqual(event.getDateAndTime())) {
            return;
        }

        event.setDateAndTime(dateAndTime);
    }

    private void updateLocation(String location, Event event) {
        if (location == null) {
            throw new UnprocessableContentException("Field 'location' cannot be empty");
        }

        if (location.equals(event.getLocation())) {
            return;
        }

        event.setLocation(location);
    }

    private void updateDescription(String description, Event event) {
        if (description == null) {
            throw new UnprocessableContentException("Field 'description' cannot be empty");
        }

        if (description.equals(event.getDescription())) {
            return;
        }

        event.setDescription(description);
    }

}
