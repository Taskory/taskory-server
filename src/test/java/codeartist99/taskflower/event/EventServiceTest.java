package codeartist99.taskflower.event;

import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.hashtag.Hashtag;
import codeartist99.taskflower.tag.model.Tag;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.UserService;
import codeartist99.taskflower.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private User user;

    @BeforeEach
    void setUp() {
        StringBuilder tempUsername;
        do {
            tempUsername = new StringBuilder();
            Random random = new Random();
            char[] charsForRandom = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            for (int i = 0; i < 10; i++) {
                tempUsername.append(charsForRandom[random.nextInt(36)]);
            }
        } while (userRepository.existsByUsername(tempUsername.toString()));
        String username = tempUsername.toString();
        String zoneId = "Asia/Seoul";
        user = User.builder()
                .username(username)
                .zoneId(zoneId)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void end() {
        userService.deleteById(user.getId());
    }

    /**
     * Test for saving event
     */
    @Test
    @DisplayName("save event and get event test")
    void save() throws EventNotFoundException {
//        Arrange
        String title = "test title";
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(10);
        LocalDateTime dueDateTime = LocalDateTime.now().plusDays(10);
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tag, hashtags, description, startDateTime, dueDateTime, location);

//        Act
        EventResponse eventResponse = eventService.save(user, saveEventRequest);

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
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(10);
        LocalDateTime dueDateTime = LocalDateTime.now().plusDays(10);
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tag, hashtags, description, startDateTime, dueDateTime, location);

//        the second event
        String title2 = "test title";
        Tag tag2 = null;
        List<Hashtag> hashtags2 = null;
        String description2 = "test description";
        LocalDateTime startDateTime2 = LocalDateTime.now().minusDays(5);
        LocalDateTime dueDateTime2 = LocalDateTime.now().plusDays(5);
        String location2 = "test location";
        SaveEventRequest saveEventRequest2 = new SaveEventRequest(title2, tag2, hashtags2, description2, startDateTime2, dueDateTime2, location2);

//        save a first event
        EventResponse eventResponse = eventService.save(user, saveEventRequest);
//        save a second event
        EventResponse eventResponse2 = eventService.save(user, saveEventRequest2);

//        Act
//        find all events
        List<EventResponse> eventResponseList = eventService.findAll(user);

//        Assert
//        find a first event
        EventResponse actualEventResponse = eventResponseList.get(0);
//        find a second event
        EventResponse actualEventResponse2 = eventResponseList.get(1);

        assertEquals(eventResponse.toString(), actualEventResponse.toString());
        assertEquals(eventResponse2.toString(), actualEventResponse2.toString());
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
        LocalDateTime startDateTime = LocalDateTime.now().minusMonths(1);
        LocalDateTime dueDateTime = LocalDateTime.now();
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, null, null, description, startDateTime, dueDateTime, location);

//        the second event - event happens through multiple months
        String title2 = "test title";
        String description2 = "test description";
        LocalDateTime startDateTime2 = LocalDateTime.now().minusMonths(1);
        LocalDateTime dueDateTime2 = LocalDateTime.now().plusMonths(1);
        String location2 = "test location";
        SaveEventRequest saveEventRequest2 = new SaveEventRequest(title2, null, null, description2, startDateTime2, dueDateTime2, location2);

        //        the third event - event for a single day
        String title3 = "test title";
        String description3 = "test description";
        LocalDateTime startDateTime3 = LocalDateTime.now();
        LocalDateTime dueDateTime3 = LocalDateTime.now();
        String location3 = "test location";
        SaveEventRequest saveEventRequest3 = new SaveEventRequest(title3, null, null, description3, startDateTime3, dueDateTime3, location3);

//        save a first event
        EventResponse eventResponse = eventService.save(user, saveEventRequest);
//        save a second event
        EventResponse eventResponse2 = eventService.save(user, saveEventRequest2);

//        save a third event
        EventResponse eventResponse3 = eventService.save(user, saveEventRequest3);

//        Act
//        find all events
        List<EventResponse> eventResponseList = eventService.findAll(user);

//        Assert
//        find a first event
        EventResponse actualEventResponse = eventResponseList.get(0);
//        find a second event
        EventResponse actualEventResponse2 = eventResponseList.get(1);

//        find a third event
        EventResponse actualEventResponse3 = eventResponseList.get(2);

        assertEquals(eventResponse.toString(), actualEventResponse.toString());
        assertEquals(eventResponse2.toString(), actualEventResponse2.toString());
        assertEquals(eventResponse3.toString(), actualEventResponse3.toString());
    }

    /**
     * Test for update an event
     */
    @Test
    @DisplayName("update an event")
    void updateTask() throws EventNotFoundException {
//        Arrange
//        to save an event
        String title = "test title";
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(10);
        LocalDateTime dueDateTime = LocalDateTime.now().plusDays(10);
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tag, hashtags, description, startDateTime, dueDateTime, location);

        EventResponse eventResponse = eventService.save(user, saveEventRequest);

//        update an event
        String updateTitle = "update title";
        Tag updateTag = null;
        List<Hashtag> updateHashtags = null;
        String updateDescription = "test description";
        LocalDateTime updateStartDateTime = LocalDateTime.now().minusDays(5);
        LocalDateTime updateDueDateTime = LocalDateTime.now().plusDays(5);
        String updateLocation = "update location";
        SaveEventRequest updateEventRequest = new SaveEventRequest(updateTitle, updateTag, updateHashtags, updateDescription, updateStartDateTime, updateDueDateTime, updateLocation);

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
        Tag tag = null;
        List<Hashtag> hashtags = null;
        String description = "test description";
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(10);
        LocalDateTime dueDateTime = LocalDateTime.now().plusDays(10);
        String location = "test location";
        SaveEventRequest saveEventRequest = new SaveEventRequest(title, tag, hashtags, description, startDateTime, dueDateTime, location);

        EventResponse eventResponse = eventService.save(user, saveEventRequest);

        Long eventId = eventResponse.getId();

//        Act
        eventService.deleteById(eventResponse.getId());

//        Assert
        assertThrows(EventNotFoundException.class, () -> eventService.getById(eventId));
    }
}