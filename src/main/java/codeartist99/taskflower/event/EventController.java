package codeartist99.taskflower.event;

import codeartist99.taskflower.event.payload.EventResponse;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.CurrentUser;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param startDate LocalDate type
     * @param dueDate LocalDate type
     * @return ResponseEntity - List<EventResponse>
     */
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
}
