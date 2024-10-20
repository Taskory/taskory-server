package codeartist99.taskflower.security.handler;

import codeartist99.taskflower.security.cookie.CookieUtil;
import codeartist99.taskflower.security.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static codeartist99.taskflower.security.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * Handles OAuth2 authentication failures.
 * Retrieves the target URL from cookies, attaches an error message as a query parameter,
 * and redirects the user to the appropriate page.
 */
@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * Constructor to inject the {@link HttpCookieOAuth2AuthorizationRequestRepository}.
     *
     * @param httpCookieOAuth2AuthorizationRequestRepository the repository to manage OAuth2 authorization cookies.
     */
    @Autowired
    public OAuth2AuthenticationFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    /**
     * Handles authentication failure by extracting the target URL from cookies,
     * appending the error message to the query parameters, and redirecting the user.
     *
     * @param request the {@link HttpServletRequest} representing the incoming request.
     * @param response the {@link HttpServletResponse} representing the outgoing response.
     * @param exception the exception thrown during the authentication failure.
     * @throws IOException if an input or output error occurs during redirection.
     * @throws ServletException if a servlet error occurs.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.info("[LOG - OAuth2AuthenticationFailureHandler] Authentication failed with exception: {}", exception.getMessage());

        // Retrieve the target URL from the cookie or default to "/"
        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/");
        log.info("[LOG] Retrieved target URL from cookie: {}", targetUrl);

        // Encode the error message for safe URL transmission
        String errorMessage = URLEncoder.encode(exception.getLocalizedMessage(), StandardCharsets.UTF_8);
        log.info("[LOG] Encoded error message: {}", errorMessage);

        // Append the error message as a query parameter and build the final redirect URL
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", errorMessage)
                .build()
                .toUriString();
        log.info("[LOG] Redirecting to: {}", targetUrl);

        // Remove the OAuth2 authorization request cookies
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);
        log.info("[LOG] Removed OAuth2 authorization request from cookies.");

        // Perform the redirect to the target URL
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        log.info("[LOG] Redirect completed.");
    }
}
