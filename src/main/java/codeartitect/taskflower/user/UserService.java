package codeartitect.taskflower.user;

import codeartitect.taskflower.Tag.TagRepository;
import codeartitect.taskflower.event.EventRepository;
import codeartitect.taskflower.flow.FlowRepository;
import codeartitect.taskflower.task.TaskService;
import codeartitect.taskflower.user.payload.UserResponse;
import codeartitect.taskflower.user.payload.SignupRequest;
import codeartitect.taskflower.user.payload.UserUpdateRequest;
import codeartitect.taskflower.user.entity.User;
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
    private final PasswordEncoder passwordEncoder;
    private final EventRepository eventRepository;
    private final FlowRepository flowRepository;
    private final TagRepository tagRepository;
    private final TaskService taskService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EventRepository eventRepository, FlowRepository flowRepository, TagRepository tagRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskService = taskService;
        this.eventRepository = eventRepository;
        this.flowRepository = flowRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * User signup
     * @param signupRequest Information for signup
     * @return UserResponse
     * @throws InvalidZoneIdException ZoneId is invalid
     * @throws UsernameAlreadyExistsException Username already exists
     */
    public UserResponse signup(SignupRequest signupRequest) throws InvalidZoneIdException, UsernameAlreadyExistsException {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (isInvalidateZoneId(signupRequest.getZoneId())) {
            throw new InvalidZoneIdException();
        }
        String encodedPassword = encode(signupRequest.getPassword());
        signupRequest.setPassword(encodedPassword);
        User user = new User(signupRequest);

        userRepository.save(user);

        return new UserResponse(user);
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
     * Update user info
     * @param userUpdateRequest Information for user info update
     * @return UserResponse
     * @throws InvalidZoneIdException ZoneId is invalid
     * @throws UsernameAlreadyExistsException Username already exists
     */
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) throws InvalidZoneIdException, UsernameAlreadyExistsException {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("user not found."));

        if (userRepository.existsByUsername(userUpdateRequest.getUsername()) &&
                !Objects.equals(userUpdateRequest.getUsername(), user.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (isInvalidateZoneId(userUpdateRequest.getZoneId())) {
            throw new InvalidZoneIdException();
        }

        user.setUsername(userUpdateRequest.getUsername());
        user.setPassword(encode(userUpdateRequest.getPassword()));
        user.setZoneId(userUpdateRequest.getZoneId());

        userRepository.save(user);

        return new UserResponse(user);
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
        userRepository.deleteById(id);
    }

    /**
     * Encode password
     * @param password Not encoded password
     * @return Encoded password
     */
    private String encode(String password) {
        return passwordEncoder.encode(password);
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
