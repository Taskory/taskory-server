package codeartitect.taskflower.user;

import codeartitect.taskflower.user.dto.UserResponse;
import codeartitect.taskflower.user.dto.UserSignupRequest;
import codeartitect.taskflower.user.dto.UserUpdateRequest;
import codeartitect.taskflower.user.entity.User;
import codeartitect.taskflower.user.exception.InvalidUsernameException;
import codeartitect.taskflower.user.exception.InvalidZoneIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Objects;

/**
 * Service for manage user info and user crud
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * User signup
     * @param userSignupRequest Information for signup
     * @return UserResponse
     * @throws InvalidZoneIdException ZoneId is invalid
     * @throws InvalidUsernameException Username already exists
     */
    public UserResponse signup(UserSignupRequest userSignupRequest) throws InvalidZoneIdException,InvalidUsernameException {
        if (userRepository.existByUsername(userSignupRequest.getUsername())) {
            throw new InvalidUsernameException();
        }

        if (isInvalidateZoneId(userSignupRequest.getZoneId())) {
            throw new InvalidZoneIdException();
        }

        userSignupRequest.setPassword(encode(userSignupRequest.getPassword()));
        User user = new User(userSignupRequest);

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
     * @throws InvalidUsernameException Username already exists
     */
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) throws InvalidZoneIdException, InvalidUsernameException {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("user not found."));

        if (userRepository.existByUsername(userUpdateRequest.getUsername())) {
            throw new InvalidUsernameException();
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
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User Not Found");
        }
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
