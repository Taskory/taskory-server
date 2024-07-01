package codeartist99.taskflower.event;

import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Save event
     * @param user User information
     * @param saveEventRequest Information to save event
     * @return EventResponse
     */
    public EventResponse save(User user, SaveEventRequest saveEventRequest) {
        Event event = new Event(user, saveEventRequest);

        eventRepository.save(event);

        return new EventResponse(event);
    }

    /**
     * Get event by event id
     * @param id Event id
     * @return EventResponse
     */
    public EventResponse getById(Long id) throws EventNotFoundException {
        Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        return new EventResponse(event);
    }

    /**
     * Find all events by user info
     * @param user User information
     * @return EventResponse list
     */
    public List<EventResponse> findAll(User user) {
        List<Event> events = eventRepository.findAllByUser(user);

        List<EventResponse> eventResponseList = new ArrayList<>();
        for (Event event : events) {
            eventResponseList.add(new EventResponse(event));
        }
        return eventResponseList;
    }

    /**
     * Find all events in period
     * @param user User information
     * @param startDate start date in period
     * @param dueDate due date in period
     * @return EventResponse list
     */
    public List<EventResponse> findAllInPeriod(User user, LocalDate startDate, LocalDate dueDate) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime dueDateTime = LocalDateTime.of(dueDate, LocalTime.MAX);
        List<Event> events = eventRepository.findAllByUserInPeriod(user, startDateTime, dueDateTime);

        List<EventResponse> eventResponseList = new ArrayList<>();
        for (Event event : events) {
            eventResponseList.add(new EventResponse(event));
        }
        return eventResponseList;
    }

    /**
     * Update event
     * @param eventId Event id
     * @param saveEventRequest Information to update event
     * @return EventResponse
     */
    public EventResponse updateEvent(Long eventId, SaveEventRequest saveEventRequest) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        event.update(saveEventRequest);

        Event updateEvent = eventRepository.save(event);
        return new EventResponse(updateEvent);
    }

    /**
     * Delete event by event id
     * @param id event id for delete
     */
    public void deleteById(Long id) throws EventNotFoundException {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else throw new EventNotFoundException();
    }


}
