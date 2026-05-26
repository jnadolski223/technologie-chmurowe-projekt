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

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        validateEventRequest(request);

        User user = userRepository
                .findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User with ID " + request.userId() + " not found"));

        Event event = Event.builder()
                .user(user)
                .title(request.title())
                .date(request.date())
                .time(request.time())
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

        validateEventRequest(request);

        if (!request.title().equals(event.getTitle())) {
            event.setTitle(request.title());
        }

        if (!request.location().equals(event.getLocation())) {
            event.setLocation(request.location());
        }

        if (!request.date().isEqual(event.getDate())) {
            event.setDate(request.date());
        }

        if (!request.time().equals(event.getTime())) {
            event.setTime(request.time());
        }

        if (!request.description().equals(event.getDescription())) {
            event.setDescription(request.description());
        }

        return EventResponse.fromEntity(event);
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID " + eventId + " not found"));

        eventRepository.delete(event);
    }

    private void validateEventRequest(EventRequest request) {
        if (request.userId() == null) {
            throw new UnprocessableContentException("Field 'userId' cannot be empty");
        }

        if (request.title() == null || request.title().isBlank()) {
            throw new UnprocessableContentException("Field 'title' cannot be empty");
        }

        if (request.location() == null || request.location().isBlank()) {
            throw new UnprocessableContentException("Field 'location' cannot be empty");
        }

        if (request.date() == null) {
            throw new UnprocessableContentException("Field 'date' cannot be empty");
        }

        if (request.time() == null) {
            throw new UnprocessableContentException("Field 'time' cannot be empty");
        }

        if (request.description() == null || request.description().isBlank()) {
            throw new UnprocessableContentException("Field 'description' cannot be empty");
        }
    }

}
