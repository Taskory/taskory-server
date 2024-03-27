package taskflower.taskflower.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import taskflower.taskflower.security.oauth2.CustomOAuth2UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;
    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;
    private final CustomOAuth2UserService customOauth2UserService;

    @Autowired
    public SecurityConfig(TokenFilter tokenFilter, HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository, CustomOAuth2UserService customOauth2UserService) {
        this.tokenFilter = tokenFilter;
        this.httpCookieAuthorizationRequestRepository = httpCookieAuthorizationRequestRepository;
        this.customOauth2UserService = customOauth2UserService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                http 기본 인증 비활성화
                .httpBasic(HttpBasicConfigurer::disable)
//                시큐리티에서 제공하는 기본 로그인 페이지 비활성화
                .formLogin(FormLoginConfigurer::disable)
//                Post 요청 및 resource 허용 설정
                .csrf(CsrfConfigurer::disable)
//                JWT 사용 -> Session 필요 없음
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                Cors 문제 해결
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()))
//                url 권한 매칭 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()                   // 권한 허용
                        .requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/task/**").permitAll()
//                        .requestMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "USER")
//                        OAuth2 Match
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())
//                jwt token filter 추가
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)

//                OAuth2 설정
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endPoint -> endPoint
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(httpCookieAuthorizationRequestRepository))
                        .redirectionEndpoint(endPoint -> endPoint
                                .baseUri("/oauth2/callback/**"))
                        .userInfoEndpoint(endPoint -> endPoint
                                .userService(customOauth2UserService)));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
