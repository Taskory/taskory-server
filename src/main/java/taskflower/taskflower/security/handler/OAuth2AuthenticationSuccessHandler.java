package taskflower.taskflower.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import taskflower.taskflower.security.service.TokenService;
import taskflower.taskflower.security.data.CookieUtil;
import taskflower.taskflower.security.data.UserPrincipal;
import taskflower.taskflower.security.data.HttpCookieOAuth2AuthorizationRequestRepository;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static taskflower.taskflower.security.data.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    @Value(value = "${app.oauth2.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository;
    private final TokenService tokenService;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository, TokenService tokenService, ObjectMapper objectMapper) {
        this.httpCookieOAuth2AuthorizationRepository = httpCookieOAuth2AuthorizationRepository;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("[LOG - OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess]");
        String targetUrl = determineTargetUrl(request, response, authentication);

        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRepository.removeAuthorizationRequest(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("[LOG - OAuth2AuthenticationSuccessHandler.determineTargetUrl]");
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalStateException("Bad Request");
        }

        String targetUri = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenService.createToken(authentication);
        log.info("[LOG - OAuth2AuthenticationSuccessHandler] token: {}", token);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (userPrincipal.isOfficialUser()) {
            return UriComponentsBuilder.fromUriString(targetUri)
                    .queryParam("token", token)
                    .build()
                    .toUriString();
        } else {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("id", userPrincipal.getId());
                data.put("name", userPrincipal.getName());
                data.put("email", userPrincipal.getEmail());
                String json = objectMapper.writeValueAsString(data);
                return UriComponentsBuilder.fromUriString(targetUri)
                        .queryParam("token", token)
                        .queryParam("forbidden", json)
                        .build()
                        .toUriString();
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }

    private boolean isAuthorizedRedirectUri(String redirectUri) {
        log.info("[LOG - OAuth2AuthenticationSuccessHandler.isAuthorizedRedirectUri]");
        URI clientRedirectUri = URI.create(redirectUri);

        return authorizedRedirectUris
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedUri = URI.create(authorizedRedirectUri);
                    log.info("[LOG - OAuth2AuthenticationSuccessHandler.isAuthorizedRedirectUri]: {}", authorizedRedirectUri);
                    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedUri.getPort() == clientRedirectUri.getPort();
                });
    }
}
