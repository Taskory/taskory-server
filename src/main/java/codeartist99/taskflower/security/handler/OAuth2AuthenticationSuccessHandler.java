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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static codeartist99.taskflower.security.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value(value = "${spring.security.oauth2.uris.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;

    private final ObjectMapper objectMapper;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository;
    private final TokenService tokenService;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRepository, TokenService tokenService, ObjectMapper objectMapper) {
        this.httpCookieOAuth2AuthorizationRepository = httpCookieOAuth2AuthorizationRepository;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                request.getParameter("code");
        String targetUrl = determineTargetUrl(request, response, authentication);

        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRepository.removeAuthorizationRequest(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalStateException("Bad Request");
        }

        String targetUri = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenService.createToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (isOfficialUser(userPrincipal)) {
            return UriComponentsBuilder.fromUriString(targetUri)
                    .queryParam("token", token)
                    .build()
                    .toUriString();
        } else {
            try {
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
                return null;
            }
        }
    }

    private boolean isOfficialUser(UserPrincipal userPrincipal) {
        List<Role> roles = userPrincipal.getRoles();
        for (Role role : roles) {
            if (role == Role.USER) return true;
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String redirectUri) {
        URI clientRedirectUri = URI.create(redirectUri);

        return authorizedRedirectUris
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedUri = URI.create(authorizedRedirectUri);
                    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedUri.getPort() == clientRedirectUri.getPort();
                });
    }
}
