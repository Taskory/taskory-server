package taskflower.taskflower.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.web.cors.CorsConfigurationSource;
import taskflower.taskflower.security.TokenFilter;
import taskflower.taskflower.security.oauth2.CustomOAuth2UserService;
import taskflower.taskflower.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import taskflower.taskflower.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import taskflower.taskflower.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${app.api-base-url}")
    private String apiBaseUrl;

    private final TokenFilter tokenFilter;
    private final CustomOAuth2UserService customOauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;
    private final CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public SecurityConfig(TokenFilter tokenFilter, CustomOAuth2UserService customOauth2UserService, OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository, @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource) {
        this.tokenFilter = tokenFilter;
        this.customOauth2UserService = customOauth2UserService;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
        this.oauth2AuthenticationFailureHandler = oauth2AuthenticationFailureHandler;
        this.httpCookieOauth2AuthorizationRequestRepository = httpCookieOauth2AuthorizationRequestRepository;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
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
                        .configurationSource(corsConfigurationSource))
//                url 권한 매칭 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(apiBaseUrl + "/auth/**").permitAll()                   // 권한 허용
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers(apiBaseUrl + "/user/**").hasRole("USER")
                        .requestMatchers(apiBaseUrl + "/task/**").hasRole("USER")
                        .anyRequest().authenticated())
//                oauth2 login
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(endPoint -> endPoint
                                .baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(httpCookieOauth2AuthorizationRequestRepository))
                        .redirectionEndpoint(endPoint -> endPoint
                                .baseUri("/oauth2/code/**"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler)

                )
//                jwt token filter 추가
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
