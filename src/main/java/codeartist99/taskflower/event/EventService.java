package codeartist99.taskflower.event;

import codeartist99.taskflower.common.Timezone;
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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
                .startDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getStartDateTime()))
                .dueDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getDueDateTime()))
                .location(saveEventRequest.getLocation())
                .timezone(Timezone.fromString(saveEventRequest.getTimezone()))
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
     * @param dateString dateString for getting monthly events
     * @return EventSummary list
     */
    public List<EventSummary> findAllMonthlyEvents(User user, String dateString) {
        // 1. Parse the dateString into LocalDateTime (UTC)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime inputDateTimeUtc = LocalDateTime.parse(dateString, formatter);

        // 2. Get the user's ZoneId
        ZoneId userZoneId = user.getTimezone().getZoneId();

        // 3. Convert input UTC date to the user's local time
        ZonedDateTime userLocalDateTime = inputDateTimeUtc.atZone(ZoneOffset.UTC).withZoneSameInstant(userZoneId);

        // 4. Find the first Sunday and the last Saturday of the month based on the user's local time
        LocalDate firstDayOfMonth = userLocalDateTime.toLocalDate().withDayOfMonth(1);
        LocalDate firstSunday = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate lastSaturday = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        // 5. Set time to 00:00:00 for start and end days
        LocalDateTime firstSundayStart = firstSunday.atStartOfDay();
        LocalDateTime lastSaturdayEnd = lastSaturday.atTime(LocalTime.MAX);

        // 6. Convert these to UTC for database querying
        ZonedDateTime firstSundayUtc = firstSundayStart.atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime lastSaturdayUtc = lastSaturdayEnd.atZone(userZoneId).withZoneSameInstant(ZoneOffset.UTC);

        // 7. Query the events from the repository using the calculated start and end times
        List<Event> events = eventRepository.findAllByUserInPeriod(
                user,
                firstSundayUtc.toLocalDateTime(),
                lastSaturdayUtc.toLocalDateTime()
        );

        // 8. Convert the list of Event objects to EventSummary and return it
        return events.stream()
                .map(EventSummary::new)
                .toList();
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
        foundEvent.setStartDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getStartDateTime()));
        foundEvent.setDueDateTime(TimeUtil.stringToLocalDateTime(saveEventRequest.getDueDateTime()));
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
