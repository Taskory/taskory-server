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
import taskflower.taskflower.security.oauth2.CustomOauth2UserService;
import taskflower.taskflower.security.oauth2.HttpCookieOauth2AuthorizationRequsetRepository;
import taskflower.taskflower.security.oauth2.Oauth2AuthenticationFailureHandler;
import taskflower.taskflower.security.oauth2.Oauth2AuthenticationSuccessHandler;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;
    private final CustomOauth2UserService customOauth2UserService;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final HttpCookieOauth2AuthorizationRequsetRepository httpCookieOauth2AuthorizationRequsetRepository;

    @Autowired
    public SecurityConfig(TokenFilter tokenFilter, CustomOauth2UserService customOauth2UserService, Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler, Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler, HttpCookieOauth2AuthorizationRequsetRepository httpCookieOauth2AuthorizationRequsetRepository) {
        this.tokenFilter = tokenFilter;
        this.customOauth2UserService = customOauth2UserService;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
        this.oauth2AuthenticationFailureHandler = oauth2AuthenticationFailureHandler;
        this.httpCookieOauth2AuthorizationRequsetRepository = httpCookieOauth2AuthorizationRequsetRepository;
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
//                http 기본 인증 disable
                .httpBasic(HttpBasicConfigurer::disable)
//                security 제공 기본 로그인 페이지 disable
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
                        .requestMatchers("/api/v1/auth/**", "/api/v1/oauth2/**").permitAll()
                        .anyRequest().authenticated())
//                oauth2 login
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(endPoint -> endPoint
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(httpCookieOauth2AuthorizationRequsetRepository))
                        .redirectionEndpoint(endPoint -> endPoint
                                .baseUri("/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler)

                )
//                jwt token filter 추가
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000, http://localhost:8080"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
