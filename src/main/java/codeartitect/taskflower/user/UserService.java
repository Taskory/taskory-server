package codeartitect.taskflower.user;

import codeartitect.taskflower.routine.repository.RoutineHistoryRepository;
import codeartitect.taskflower.routine.repository.RoutineRepository;
import codeartitect.taskflower.tag.TagRepository;
import codeartitect.taskflower.event.EventRepository;
import codeartitect.taskflower.flow.FlowRepository;
import codeartitect.taskflower.task.service.TaskService;
import codeartitect.taskflower.user.payload.ProfileUpdateRequest;
import codeartitect.taskflower.user.payload.UserResponse;
import codeartitect.taskflower.user.model.User;
import codeartitect.taskflower.user.exception.UsernameAlreadyExistsException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for manage user info and user crud
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final FlowRepository flowRepository;
    private final TagRepository tagRepository;
    private final TaskService taskService;
    private final RoutineRepository routineRepository;
    private final RoutineHistoryRepository routineHistoryRepository;

    @Autowired
    public UserService(UserRepository userRepository, EventRepository eventRepository, FlowRepository flowRepository, TagRepository tagRepository, TaskService taskService, RoutineRepository routineRepository, RoutineHistoryRepository routineHistoryRepository) {
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.eventRepository = eventRepository;
        this.flowRepository = flowRepository;
        this.tagRepository = tagRepository;
        this.routineRepository = routineRepository;
        this.routineHistoryRepository = routineHistoryRepository;
    }

    /**
     * Get user by username
     * @param username Username
     * @return UserResponse
     */
    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new UserResponse(user);
    }

    /**
     * Update user profile
     * @param userId User id
     * @param profileUpdateRequest Information for profile update
     * @return UserResponse
     */
    public UserResponse updateProfile(Long userId, ProfileUpdateRequest profileUpdateRequest) throws UsernameAlreadyExistsException, InvalidZoneIdException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userRepository.existsByUsername(profileUpdateRequest.getUsername()) && !Objects.equals(profileUpdateRequest.getUsername(), user.getUsername())) {
                throw new UsernameAlreadyExistsException();
        }

        if (isInvalidateZoneId(profileUpdateRequest.getZoneId())) {
            throw new InvalidZoneIdException();
        }

        user.updateProfile(profileUpdateRequest);
        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }

    /**
     * Delete user
     * @param id User id for delete
     */
    @Transactional
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        tagRepository.deleteAllByUser(user.get());
        flowRepository.deleteAllByUser(user.get());
        taskService.deleteAllByUser(user.get());
        eventRepository.deleteAllByUser(user.get());
        routineHistoryRepository.deleteAllByUser(user.get());
        routineRepository.deleteAllByUser(user.get());
        userRepository.deleteById(id);
    }

    /**
     * Validate zone id
     * @param zoneId Zone id requested from user
     * @return boolean
     */
    private boolean isInvalidateZoneId(String zoneId) {
        for (String zone : ZoneId.getAvailableZoneIds()) {
            if (Objects.equals(zone, zoneId)) return false;
        }
        return true;
    }

}
