package codeartist99.taskflower.event;

import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.CurrentUser;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.url-base}/event")
public class EventController {

    private final UserRepository userRepository;
    private final EventService eventService;

    @Autowired
    public EventController(UserRepository userRepository, EventService eventService) {
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    @GetMapping("/period")
    public ResponseEntity<List<EventSummary>> findAllEventsInPeriod(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString) {

        try {
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

            List<EventSummary> events = eventService.findEventsInPeriod(user, startDateString, endDateString);
            return ResponseEntity.ok(events);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (DateTimeParseException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    /**
     * Create a new event.
     * @param userPrincipal Authenticated user
     * @param saveEventRequest Event data to be saved
     * @return The saved EventResponse
     */
    @PostMapping("/create")
    public ResponseEntity<EventResponse> create(@CurrentUser UserPrincipal userPrincipal, @RequestBody SaveEventRequest saveEventRequest) {
        try {
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            EventResponse response = eventService.save(user, saveEventRequest);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Update an existing event.
     * @param userPrincipal Authenticated user
     * @param eventId ID of the event to be updated
     * @param saveEventRequest Updated event data
     * @return The updated EventResponse
     */
    @PutMapping("/update")
    public ResponseEntity<EventResponse> update(@CurrentUser UserPrincipal userPrincipal, @RequestParam("eventId") Long eventId, @RequestBody SaveEventRequest saveEventRequest) {
        try {
            EventResponse response = eventService.updateEvent(eventId, saveEventRequest);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an event by ID.
     * @param eventId ID of the event to be deleted
     * @return ResponseEntity with HTTP status
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@CurrentUser UserPrincipal userPrincipal, @RequestParam("eventId") Long eventId) {
        try {
            eventService.deleteById(eventId);
            return ResponseEntity.ok().build();
        } catch (EventNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get an event by its ID.
     * @param eventId ID of the event
     * @return The EventResponse
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getById(@PathVariable("eventId") Long eventId) {
        try {
            EventResponse event = eventService.getById(eventId);
            return ResponseEntity.ok(event); // Responds with the event if found
        } catch (UsernameNotFoundException | EventNotFoundException exception) {
            return ResponseEntity.notFound().build(); // Returns 404 if event is not found
        }
    }

    /**
     * Get all events for the authenticated user.
     * @param userPrincipal Authenticated user
     * @return List of EventResponse
     */
    @GetMapping("/all")
    public ResponseEntity<List<EventSummary>> findAll(@CurrentUser UserPrincipal userPrincipal) {
        try {
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            List<EventSummary> events = eventService.findAll(user);
            return ResponseEntity.ok(events);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
