package codeartist99.taskflower.event;

import codeartist99.taskflower.common.util.TimeUtil;
import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.hashtag.HashtagRepository;
import codeartist99.taskflower.tag.TagRepository;
import codeartist99.taskflower.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final HashtagRepository hashtagRepository;

    @Autowired
    public EventService(EventRepository eventRepository, TagRepository tagRepository, HashtagRepository hashtagRepository) {
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
        this.hashtagRepository = hashtagRepository;
    }

    /**
     * Save event
     * @param user User information
     * @param saveEventRequest Information to save event
     * @return EventResponse
     */
    public EventResponse save(User user, SaveEventRequest saveEventRequest) {
        if (saveEventRequest.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }

        Event event = Event.builder()
                .title(saveEventRequest.getTitle())
                .description(saveEventRequest.getDescription())
                .startDateTime(TimeUtil.StringToLocalDateTime(saveEventRequest.getStartDateTime()))
                .dueDateTime(TimeUtil.StringToLocalDateTime(saveEventRequest.getDueDateTime()))
                .location(saveEventRequest.getLocation())
                .timezone(saveEventRequest.getTimezone())
                .user(user)
                .build();

        if (saveEventRequest.getTagId() != null) {
            event.setTag(tagRepository.findById(saveEventRequest.getTagId()).orElse(null));
        } else {
            event.setTag(null);
        }
        if (saveEventRequest.getHashtagIds() != null) {
            event.setHashtags(hashtagRepository.findAllById(saveEventRequest.getHashtagIds()));
        } else {
            event.setHashtags(null);
        }

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
     * find monthly events
     * @param user User information
     * @param isoDateString isoDateString for getting monthly events
     * @return EventSummary list
     */
    public List<EventSummary> findAllMonthlyEvents(User user, String isoDateString) {
        log.info("[LOG - EventService.findAllMonthlyEvent] isoDateString: {}", isoDateString);
        try {
            LocalDateTime dateTime = TimeUtil.StringToLocalDateTime(isoDateString);
            log.info("[LOG - EventService.findAllMonthlyEvent] dateTime: {}", dateTime);

            LocalDateTime firstDateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0, 0);
            log.info("[LOG - EventService.findAllMonthlyEvent] firstDateTime: {}", firstDateTime);
            LocalDateTime lastDateTime = YearMonth.from(dateTime).atEndOfMonth().atTime(23, 59, 59);
            log.info("[LOG - EventService.findAllMonthlyEvent] lastDateTime: {}", lastDateTime);
            List<Event> events = eventRepository.findAllByUserInPeriod(user, firstDateTime, lastDateTime);
            log.info("[LOG - EventService.findAllMonthlyEvent] events.size: {}", events.size());
            return events.stream()
                    .map(EventSummary::new)
                    .toList();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid ISO date string: " + isoDateString);
        } catch (ZoneRulesException e) {
            throw new IllegalArgumentException("Invalid time zone: " + user.getZoneId());
        }
    }

    /**
     * Update event
     * @param eventId Event id
     * @param saveEventRequest Information to update event
     * @return EventResponse
     */
    @Transactional
    public EventResponse updateEvent(Long eventId, SaveEventRequest saveEventRequest) throws EventNotFoundException {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);

        foundEvent.setTitle(saveEventRequest.getTitle());
        if (saveEventRequest.getTagId() != null) {
            foundEvent.setTag(tagRepository.findById(saveEventRequest.getTagId()).orElse(null));
        } else {
            foundEvent.setTag(null);
        }
        if (saveEventRequest.getHashtagIds() != null) {
            foundEvent.setHashtags(hashtagRepository.findAllById(saveEventRequest.getHashtagIds()));
        } else {
            foundEvent.setHashtags(null);
        }
        foundEvent.setDescription(saveEventRequest.getDescription());
        foundEvent.setStartDateTime(TimeUtil.StringToLocalDateTime(saveEventRequest.getStartDateTime()));
        foundEvent.setDueDateTime(TimeUtil.StringToLocalDateTime(saveEventRequest.getDueDateTime()));
        foundEvent.setLocation(saveEventRequest.getLocation());

        Event result = eventRepository.save(foundEvent);
        return new EventResponse(result);
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
