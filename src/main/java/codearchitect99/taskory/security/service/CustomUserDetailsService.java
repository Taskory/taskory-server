package codearchitect99.taskory.security.service;

import codearchitect99.taskory.security.model.UserPrincipal;
import codearchitect99.taskory.user.UserRepository;
import codearchitect99.taskory.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of the {@link UserDetailsService} interface.
 * This service is responsible for loading user-specific data during authentication.
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor to inject the {@link UserRepository} dependency.
     *
     * @param userRepository the repository used to retrieve user data from the database.
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user's details by their username.
     *
     * @param username the username of the user to be loaded.
     * @return a {@link UserDetails} object containing the user's information.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[LOG] Loading user by username: {}", username);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("[LOG] User found: {}", user.get().getUsername());
            return new UserPrincipal(user.get());
        } else {
            log.error("[LOG] User not found for username: {}", username);
            throw new UsernameNotFoundException("User not found.");
        }
    }

    /**
     * Loads a user's details by their user ID.
     *
     * @param userId the ID of the user to be loaded.
     * @return a {@link UserDetails} object containing the user's information.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    public UserDetails loadUserByUserId(Long userId) {
        log.info("[LOG] Loading user by ID: {}", userId);

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("[LOG] User found: {}", user.get().getUsername());
            return new UserPrincipal(user.get());
        } else {
            log.error("[LOG] User not found for ID: {}", userId);
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
