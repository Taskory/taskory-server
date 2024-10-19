package codeartist99.taskflower.security.service;

import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.user.UserRepository;
import codeartist99.taskflower.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new UserPrincipal(user.get());
        } else throw new UsernameNotFoundException("User not found.");
    }

    public UserDetails loadUserByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        log.info("[LOG] CustomUserDetailsService.loadUserByUserId [{}]", userId);
        if (user.isPresent()) {
            return new UserPrincipal(user.get());
        } else throw new UsernameNotFoundException("User not found.");
    }
}
