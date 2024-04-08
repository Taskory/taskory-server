package taskflower.taskflower.security.oauth2.handler;

import jakarta.servlet.ServletException;
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
import taskflower.taskflower.security.TokenProvider;
import taskflower.taskflower.security.CookieUtils;
import taskflower.taskflower.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static taskflower.taskflower.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value(value = "${app.oauth2.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository, TokenProvider tokenProvider) {
        this.httpCookieOAuth2AuthorizationRepository = httpCookieOAuth2AuthorizationRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRepository.removeAuthorizationRequest(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            log.info("[LOG - OAuth2AuthenticationSuccessHandler]");
            throw new IllegalStateException("Bad Request");
        }

        String targetUri = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUri)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    private boolean isAuthorizedRedirectUri(String redirectUri) {
        URI clientRedirectUri = URI.create(redirectUri);

        return authorizedRedirectUris
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedUri = URI.create(authorizedRedirectUri);
                    log.info("[LOG - OAuth2AuthenticationSuccessHandler.isAuthorizedRedirectUri]: " + authorizedRedirectUri);
                    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedUri.getPort() == clientRedirectUri.getPort();
                });
    }
}
