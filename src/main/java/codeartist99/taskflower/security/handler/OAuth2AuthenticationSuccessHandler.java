package codeartist99.taskflower.security.handler;

import codeartist99.taskflower.security.cookie.CookieUtil;
import codeartist99.taskflower.security.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import codeartist99.taskflower.security.model.UserPrincipal;
import codeartist99.taskflower.security.token.TokenService;
import codeartist99.taskflower.user.model.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static codeartist99.taskflower.security.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * Handles successful OAuth2 authentication.
 * Generates a token, manages redirection, and clears authorization-related cookies.
 */
@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.security.oauth2.uris.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;

    private final ObjectMapper objectMapper;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository;
    private final TokenService tokenService;

    /**
     * Constructor for injecting dependencies.
     *
     * @param httpCookieOAuth2AuthorizationRepository repository to manage OAuth2 authorization cookies.
     * @param tokenService service to create and manage JWT tokens.
     * @param objectMapper object mapper for JSON serialization.
     */
    @Autowired
    public OAuth2AuthenticationSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository,
                                              TokenService tokenService,
                                              ObjectMapper objectMapper) {
        this.httpCookieOAuth2AuthorizationRepository = httpCookieOAuth2AuthorizationRepository;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    /**
     * Handles the successful OAuth2 authentication.
     * Clears authentication attributes, removes cookies, and redirects the user to the target URL.
     *
     * @param request the HttpServletRequest representing the incoming request.
     * @param response the HttpServletResponse representing the outgoing response.
     * @param authentication the authentication object containing the authenticated user's details.
     * @throws IOException if an input or output exception occurs during the redirection.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("[LOG] OAuth2 authentication succeeded for user: {}", authentication.getName());

        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("[LOG] Redirecting to target URL: {}", targetUrl);

        super.clearAuthenticationAttributes(request);
        log.info("[LOG] Cleared authentication attributes.");

        httpCookieOAuth2AuthorizationRepository.removeAuthorizationRequest(request, response);
        log.info("[LOG] Removed OAuth2 authorization request from cookies.");

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * Determines the target URL to redirect the user after successful authentication.
     * Validates the redirect URI and appends the generated token or forbidden information if necessary.
     *
     * @param request the HttpServletRequest representing the incoming request.
     * @param response the HttpServletResponse representing the outgoing response.
     * @param authentication the authentication object containing the authenticated user's details.
     * @return the target URL as a string.
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("[LOG] Determining target URL after successful authentication.");

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        redirectUri.ifPresent(uri -> log.info("[LOG] Found redirect URI from cookie: {}", uri));

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            log.error("[LOG] Unauthorized redirect URI: {}", redirectUri.get());
            throw new IllegalStateException("Bad Request");
        }

        String targetUri = redirectUri.orElse(getDefaultTargetUrl());
        log.info("[LOG] Using target URI: {}", targetUri);

        String token = tokenService.createToken(authentication);
        log.info("[LOG] Created token: {}", token);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        log.info("[LOG] Retrieved user principal: {}", userPrincipal.getUsername());

        if (isOfficialUser(userPrincipal)) {
            log.info("[LOG] User is authorized. Redirecting with token.");
            return UriComponentsBuilder.fromUriString(targetUri)
                    .queryParam("token", token)
                    .build()
                    .toUriString();
        } else {
            try {
                log.info("[LOG] User is not an official user. Adding forbidden information.");
                Map<String, Object> data = new HashMap<>();
                data.put("id", userPrincipal.getId());
                data.put("username", userPrincipal.getUsername());

                String json = objectMapper.writeValueAsString(data);
                return UriComponentsBuilder.fromUriString(targetUri)
                        .queryParam("token", token)
                        .queryParam("forbidden", json)
                        .build()
                        .toUriString();
            } catch (JsonProcessingException e) {
                log.error("[LOG] Error serializing forbidden data: {}", e.getMessage());
                return null;
            }
        }
    }

    /**
     * Checks if the given user has the official role.
     *
     * @param userPrincipal the user's principal containing roles and other details.
     * @return true if the user has the {@link Role#USER} role; false otherwise.
     */
    private boolean isOfficialUser(UserPrincipal userPrincipal) {
        log.info("[LOG] Checking if user is official: {}", userPrincipal.getUsername());
        return userPrincipal.getRoles().stream().anyMatch(role -> role == Role.USER);
    }

    /**
     * Validates if the provided redirect URI is among the authorized URIs.
     *
     * @param redirectUri the redirect URI to validate.
     * @return true if the redirect URI is authorized; false otherwise.
     */
    private boolean isAuthorizedRedirectUri(String redirectUri) {
        log.info("[LOG] Validating redirect URI: {}", redirectUri);

        URI clientRedirectUri = URI.create(redirectUri);

        boolean isAuthorized = authorizedRedirectUris.stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedUri = URI.create(authorizedRedirectUri);
                    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedUri.getPort() == clientRedirectUri.getPort();
                });

        log.info("[LOG] Redirect URI authorized: {}", isAuthorized);
        return isAuthorized;
    }
}
