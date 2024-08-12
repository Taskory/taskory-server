package codeartist99.taskflower.event;

import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.event.payload.EventSummary;
import codeartist99.taskflower.event.payload.SaveEventRequest;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.CurrentUser;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import com.sun.jdi.request.EventRequest;
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
     * @param userPrincipal Authenticated user
     * @param startDate     LocalDate type
     * @param dueDate       LocalDate type
     * @return ResponseEntity - List<EventResponse>
     */
//    need to update parameter -> not period but each month
    @GetMapping
    public ResponseEntity<?> findAllEventsInPeriod(@CurrentUser UserPrincipal userPrincipal, @RequestParam LocalDate startDate, @RequestParam LocalDate dueDate) {
        try {
            User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            List<EventResponse> events = eventService.findAllInPeriod(user, startDate, dueDate);
            return ResponseEntity.ok().body(events);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }

    /**
     * @param userPrincipal Authenticated user
     * @param date          LocalDate for getting monthly events
     * @return ResponseEntity - EventSummary list
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
     * get monthly events
     *
     * @param userPrincipal    Authenticated user
     * @param saveEventRequest Event info for save event
     * @return EventResponse
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
     * put request for update event
     *
     * @param eventId          event id for update event
     * @param saveEventRequest event info for update
     * @return EventResponse
     */
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long eventId, @RequestBody SaveEventRequest saveEventRequest) {
        try {
            EventResponse response = eventService.updateEvent(eventId, saveEventRequest);
            return ResponseEntity.ok().body(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * delete events
     * @param eventId event id for delete event
     * @return ResponseEntity.status 200
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long eventId) {
        try {
            eventService.deleteById(eventId);
            return ResponseEntity.ok().build();
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}