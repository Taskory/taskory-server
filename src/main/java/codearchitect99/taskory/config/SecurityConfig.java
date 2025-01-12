package codearchitect99.taskory.config;

import codearchitect99.taskory.security.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import codearchitect99.taskory.security.handler.OAuth2AuthenticationFailureHandler;
import codearchitect99.taskory.security.handler.OAuth2AuthenticationSuccessHandler;
import codearchitect99.taskory.security.service.CustomOAuth2UserService;
import codearchitect99.taskory.security.token.TokenFilter;
import codearchitect99.taskory.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration class for the application.
 * Configures authentication, authorization, CORS, session management, and OAuth2 settings.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        proxyTargetClass = true,
        jsr250Enabled = true
)
public class SecurityConfig {

    @Value("${app.url-base}")
    private String urlBase;

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    private final List<String> allowedMethods = Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name()
    );

    private final List<String> allowedHeaders = Arrays.asList(
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.AUTHORIZATION
    );

    private final TokenFilter tokenFilter;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    /**
     * Constructor to inject security-related dependencies.
     *
     * @param tokenFilter the JWT token filter to validate tokens.
     * @param httpCookieOAuth2AuthorizationRequestRepository the repository for managing OAuth2 authorization cookies.
     * @param customOAuth2UserService the service to manage OAuth2 user information.
     * @param oAuth2AuthenticationSuccessHandler handler for OAuth2 authentication success events.
     * @param oAuth2AuthenticationFailureHandler handler for OAuth2 authentication failure events.
     */
    @Autowired
    public SecurityConfig(TokenFilter tokenFilter,
                          HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.tokenFilter = tokenFilter;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    /**
     * Creates and configures the authentication manager bean.
     *
     * @param authConfig the authentication configuration.
     * @return the authentication manager.
     * @throws Exception if there is an error during the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param httpSecurity the {@link HttpSecurity} object to configure security settings.
     * @return the configured security filter chain.
     * @throws Exception if there is an error during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Basic security settings
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable) // Disable HTTP Basic authentication
                .formLogin(FormLoginConfigurer::disable) // Disable form-based login
                .csrf(CsrfConfigurer::disable) // Disable CSRF (as this is a stateless REST API)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Use stateless session management

        // CORS configuration
        httpSecurity.cors(corsConfigurer ->
                corsConfigurer.configurationSource(corsConfigurationSource()));

        // OAuth2 login configuration
        httpSecurity.oauth2Login(oauth2LoginConfigurer ->
                oauth2LoginConfigurer
                        .authorizationEndpoint(endpointConfig ->
                                endpointConfig.baseUri("/oauth2/authorize")
                                        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .redirectionEndpoint(redirectionEndpointConfig ->
                                redirectionEndpointConfig.baseUri("/oauth2/code/**"))
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler));

        // Request authorization settings
        // TODO: The request is not working due to role management issues
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth2/**").permitAll() // Permit all OAuth2 requests
                .requestMatchers(urlBase + "/auth/**").permitAll() // Permit all auth-related requests
                .requestMatchers(urlBase + "/user/**").hasAnyAuthority(Role.USER.getName(), Role.TEMP_USER.getName(), Role.ADMIN.getName()) // User roles access
                .requestMatchers(urlBase + "/event/**").hasAnyAuthority(Role.USER.getName(), Role.ADMIN.getName()) // Event access
                .requestMatchers(urlBase + "/task/**").hasAnyAuthority(Role.USER.getName(), Role.ADMIN.getName()) // Task access
                .requestMatchers(urlBase + "/tag/**").hasAnyAuthority(Role.USER.getName(), Role.ADMIN.getName()) // Tag access
                .anyRequest().denyAll()); // Deny all other requests

        // Add the JWT token filter before UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Configures the CORS settings for the application.
     *
     * @return a {@link CorsConfigurationSource} with the configured CORS settings.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(true); // Allow credentials for CORS requests

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(urlBase + "/**", configuration); // Apply CORS settings to all routes

        return source;
    }
}
