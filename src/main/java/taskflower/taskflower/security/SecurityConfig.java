package taskflower.taskflower.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api.version}")
    private String BASE_URL;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(BASE_URL + "/", BASE_URL + "/login").permitAll()
                        .requestMatchers(BASE_URL + "/admin/**").hasRole("ADMIN")
                        .requestMatchers(BASE_URL + "/user/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated());
        return http.build();
    }
}
