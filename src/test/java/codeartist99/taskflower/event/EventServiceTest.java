package codeartist99.taskflower.event;

import codeartist99.taskflower.common.util.TimeUtil;
import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.setup.ArrangeTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest extends ArrangeTest {

    @Autowired
    private EventService eventService;

    /**
     * Test for saving event
     */
    @Test
    @DisplayName("save event and get event test")
    @Transactional
    void save() throws EventNotFoundException {
//        Arrange
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        String startDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().minusDays(10));
        String dueDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().plusDays(10));
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime, dueDateTime, location);

//        Act
        EventResponse eventResponse = eventService.save(tempUser, saveEventRequest);

//        Assert
        assertEquals(eventResponse.toString(), eventService.getById(eventResponse.getId()).toString());
    }

    /**
     * Test for find all events by user
     */
    @Test
    @DisplayName("find all by user test")
    void findAll() {
//        Arrange
//        the first event
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        String startDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().minusDays(10)); // 변환
        String dueDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().plusDays(10)); // 변환
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime, dueDateTime, location);

//        the second event
        String title2 = "test title";
        List<Long> hashtags2 = Collections.emptyList();
        String description2 = "test description";
        String startDateTime2 = TimeUtil.localDateTimeToString(LocalDateTime.now().minusDays(5)); // 변환
        String dueDateTime2 = TimeUtil.localDateTimeToString(LocalDateTime.now().plusDays(5)); // 변환
        String location2 = "test location";
        SaveEventRequest saveEventRequest2 = new SaveEventRequest(title2, tempTag.getId(), hashtags2, description2, startDateTime2, dueDateTime2, location2);

//        save a first event
        EventResponse eventResponse = eventService.save(tempUser, saveEventRequest);
//        save a second event
        EventResponse eventResponse2 = eventService.save(tempUser, saveEventRequest2);

//        Act
//        find all events
        List<EventSummary> eventResponseList = eventService.findAll(tempUser);

//        Assert
//        find a first event
        boolean actualEventResponseFound = eventResponseList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse.getId()));
//        find a second event
        boolean actualEventResponseFound2 = eventResponseList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse2.getId()));

        assertTrue(actualEventResponseFound);
        assertTrue(actualEventResponseFound2);
    }


    /**
     * Test for find all events in period
     */
    @Test
    @DisplayName("find all in period test")
    void findAllInPeriod() {
//        Arrange
//        the first event - event happens through multiple months
        String title = "test title";
        String description = "test description";
        String startDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().minusMonths(1)); // 변환
        String dueDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now()); // 변환
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tempTag.getId(), Collections.emptyList(), description, startDateTime, dueDateTime, location);

//        the second event - event happens through multiple months
        String title2 = "test title";
        String description2 = "test description";
        String startDateTime2 = TimeUtil.localDateTimeToString(LocalDateTime.now().minusMonths(1)); // 변환
        String dueDateTime2 = TimeUtil.localDateTimeToString(LocalDateTime.now().plusMonths(1)); // 변환
        String location2 = "test location";
        SaveEventRequest saveEventRequest2 = new SaveEventRequest(title2, tempTag.getId(), Collections.emptyList(), description2, startDateTime2, dueDateTime2, location2);

//        the third event - event for a single day
        String title3 = "test title";
        String description3 = "test description";
        String startDateTime3 = TimeUtil.localDateTimeToString(LocalDateTime.now()); // 변환
        String dueDateTime3 = TimeUtil.localDateTimeToString(LocalDateTime.now()); // 변환
        String location3 = "test location";
        SaveEventRequest saveEventRequest3 = new SaveEventRequest(title3, tempTag.getId(), Collections.emptyList(), description3, startDateTime3, dueDateTime3, location3);

//        save a first event
        EventResponse eventResponse = eventService.save(tempUser, saveEventRequest);
//        save a second event
        EventResponse eventResponse2 = eventService.save(tempUser, saveEventRequest2);

//        save a third event
        EventResponse eventResponse3 = eventService.save(tempUser, saveEventRequest3);

//        Act
//        find all events
        List<EventSummary> eventSummaryList = eventService.findAll(tempUser);

//        Assert
//        find a first event
        boolean actualEventResponseFound = eventSummaryList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse.getId()));
//        find a second event
        boolean actualEventResponseFound2 = eventSummaryList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse2.getId()));
//        find a third event
        boolean actualEventResponseFound3 = eventSummaryList.stream()
                        .anyMatch(event -> event.getId().equals(eventResponse3.getId()));

        assertTrue(actualEventResponseFound);
        assertTrue(actualEventResponseFound2);
        assertTrue(actualEventResponseFound3);
    }

    /**
     * Test for update an event
     */
    @Test
    @DisplayName("update an event")
    void updateEvent() throws EventNotFoundException {
//        Arrange
//        to save an event
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        String startDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().minusDays(10));
        String dueDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().plusDays(10));
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime, dueDateTime, location);

        EventResponse eventResponse = eventService.save(tempUser, saveEventRequest);

//        update an event
        String updateTitle = "update title";
        List<Long> updateHashtags = Collections.emptyList();
        String updateDescription = "test description";
        String updateStartDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().minusDays(5));
        String updateDueDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().plusDays(5));
        String updateLocation = "update location";
        SaveEventRequest updateEventRequest = new SaveEventRequest(updateTitle, tempTag.getId(), updateHashtags, updateDescription, updateStartDateTime, updateDueDateTime, updateLocation);

//        Act
        EventResponse updateEventResponse = eventService.updateEvent(eventResponse.getId(), updateEventRequest);

//        Assert
        assertEquals(updateEventResponse.toString(), eventService.getById(eventResponse.getId()).toString());
    }

    /**
     * Test for delete an event
     */
    @Test
    @DisplayName("delete event")
    void deleteById() throws EventNotFoundException {
//        Arrange
//        save a task
        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        String startDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().minusDays(10));
        String dueDateTime = TimeUtil.localDateTimeToString(LocalDateTime.now().plusDays(10));
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime, dueDateTime, location);

        EventResponse eventResponse = eventService.save(tempUser, saveEventRequest);

        Long eventId = eventResponse.getId();

//        Act
        eventService.deleteById(eventResponse.getId());

//        Assert
        assertThrows(EventNotFoundException.class, () -> eventService.getById(eventId));
    }

    /**
     * Test for find all monthly events
     */
    @Test
    @DisplayName("find all monthly events test")
    void findEventsInPeriod() {
//        Arrange
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startTime = currentDateTime.with(firstDayOfMonth()).with(LocalTime.MIN); // 당월 1일 00:00:00
        LocalDateTime endTime = currentDateTime.with(lastDayOfMonth()).with(LocalTime.MAX); // 당월 마지막날 23:59:59
        String firstDayOfMonth = TimeUtil.localDateTimeToString(startTime);
        String lastDayOfMonth = TimeUtil.localDateTimeToString(endTime);

        String title = "test title";
        List<Long> hashtags = Collections.emptyList();
        String description = "test description";
        String location = "test location";

        // Event 1 within the same month
        String startDateTime1 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(firstDayOfMonth));
        String dueDateTime1 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(startDateTime1).plusDays(10));
        SaveEventRequest saveEventRequest1 = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime1, dueDateTime1, location);

        // Event 2 within the same month
        String startDateTime2 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(startDateTime1).plusDays(15));
        String dueDateTime2 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(startDateTime2).plusDays(5));
        SaveEventRequest saveEventRequest2 = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime2, dueDateTime2, location);

        // Event 3 starting in the previous month and ending in the current month
        String startDateTime3 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(startDateTime1).minusDays(5));
        String dueDateTime3 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(startDateTime1).plusDays(5));
        SaveEventRequest saveEventRequest3 = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime3, dueDateTime3, location);

        // Event 4 starting in the current month and ending in the next month
        String startDateTime4 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(lastDayOfMonth).minusDays(5));
        String dueDateTime4 = TimeUtil.localDateTimeToString(TimeUtil.stringToLocalDateTime(lastDayOfMonth).plusDays(5));
        SaveEventRequest saveEventRequest4 = new SaveEventRequest(title, tempTag.getId(), hashtags, description, startDateTime4, dueDateTime4, location);

        EventResponse eventResponse1 = eventService.save(tempUser, saveEventRequest1);
        EventResponse eventResponse2 = eventService.save(tempUser, saveEventRequest2);
        EventResponse eventResponse3 = eventService.save(tempUser, saveEventRequest3);
        EventResponse eventResponse4 = eventService.save(tempUser, saveEventRequest4);

//        Act
        List<EventSummary> eventSummaryList = eventService.findEventsInPeriod(tempUser, firstDayOfMonth, lastDayOfMonth);

//        Assert
        boolean isFoundEvent1 = eventSummaryList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse1.getId()));

        boolean isFoundEvent2 = eventSummaryList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse2.getId()));

        boolean isFoundEvent3 = eventSummaryList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse3.getId()));

        boolean isFoundEvent4 = eventSummaryList.stream()
                .anyMatch(event -> event.getId().equals(eventResponse4.getId()));

        assertTrue(isFoundEvent1);
        assertTrue(isFoundEvent2);
        assertTrue(isFoundEvent3);
        assertTrue(isFoundEvent4);
    }
}