package codeartist99.taskflower.event;

import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.CurrentUser;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * Get all events in a specific month.
     * @param userPrincipal Authenticated user
     * @param date Date within the month to fetch events
     * @return List of EventSummary
     */
    @GetMapping("/month")
    public ResponseEntity<?> findAllMonthlyEvents(@CurrentUser UserPrincipal userPrincipal, @RequestParam LocalDate date) {
        try {
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            List<EventSummary> events = eventService.findAllMonthlyEvents(user, date);
            return ResponseEntity.ok().body(events);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }

    /**
     * Create a new event.
     * @param userPrincipal Authenticated user
     * @param saveEventRequest Event data to be saved
     * @return The saved EventResponse
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@CurrentUser UserPrincipal userPrincipal, @RequestBody SaveEventRequest saveEventRequest) {
        try {
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            EventResponse response = eventService.save(user, saveEventRequest);
            return ResponseEntity.ok().body(response);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
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
    public ResponseEntity<?> update(@CurrentUser UserPrincipal userPrincipal, @RequestParam Long eventId, @RequestBody SaveEventRequest saveEventRequest) {
        try {
            EventResponse response = eventService.updateEvent(eventId, saveEventRequest);
            return ResponseEntity.ok().body(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Delete an event by ID.
     * @param eventId ID of the event to be deleted
     * @return ResponseEntity with HTTP status
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@CurrentUser UserPrincipal userPrincipal, @RequestParam Long eventId) {
        try {
            eventService.deleteById(eventId);
            return ResponseEntity.ok().build();
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Get an event by its ID.
     * @param eventId ID of the event
     * @return The EventResponse
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getById(@PathVariable Long eventId) {
        try {
            EventResponse event = eventService.getById(eventId);
            return ResponseEntity.ok().body(event);
        } catch (UsernameNotFoundException | EventNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    /**
     * Get all events for the authenticated user.
     * @param userPrincipal Authenticated user
     * @return List of EventResponse
     */
    @GetMapping("/all")
    public ResponseEntity<?> findAll(@CurrentUser UserPrincipal userPrincipal) {
        try {
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            List<EventResponse> events = eventService.findAll(user);
            return ResponseEntity.ok().body(events);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }
}
