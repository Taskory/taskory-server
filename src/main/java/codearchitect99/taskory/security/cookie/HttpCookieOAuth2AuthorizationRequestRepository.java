package codearchitect99.taskory.security.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A repository to manage the OAuth2 authorization requests stored in cookies.
 * Handles saving, loading, and removing the authorization request cookies.
 */
@Component
@Slf4j
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final int COOKIE_EXPIRE_SECONDS = 100;

    /**
     * Loads the OAuth2 authorization request from the cookies in the incoming request.
     *
     * @param request the {@link HttpServletRequest} containing the cookies.
     * @return the {@link OAuth2AuthorizationRequest} if found; otherwise, null.
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("[LOG] Loading OAuth2 authorization request from cookie: {}", OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> {
                    log.info("[LOG] OAuth2 authorization request cookie found.");
                    return CookieUtil.deserialize(cookie.getValue(), OAuth2AuthorizationRequest.class);
                })
                .orElseGet(() -> {
                    log.warn("[LOG] No OAuth2 authorization request cookie found.");
                    return null;
                });
    }

    /**
     * Saves the OAuth2 authorization request to cookies.
     * If the authorization request is null, it deletes the relevant cookies.
     *
     * @param authorizationRequest the {@link OAuth2AuthorizationRequest} to save.
     * @param request the {@link HttpServletRequest} containing the original request.
     * @param response the {@link HttpServletResponse} to which cookies will be added.
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            log.info("[LOG] Authorization request is null. Deleting cookies.");
            CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        log.info("[LOG] Saving OAuth2 authorization request to cookie.");
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.hasLength(redirectUriAfterLogin)) {
            log.info("[LOG] Saving redirect URI to cookie: {}", redirectUriAfterLogin);
            CookieUtil.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        } else {
            log.warn("[LOG] No redirect URI parameter found in the request.");
        }
    }

    /**
     * Removes the OAuth2 authorization request by loading it from the cookies.
     *
     * @param request the {@link HttpServletRequest} containing the cookies.
     * @param response the {@link HttpServletResponse} to modify the cookies.
     * @return the removed {@link OAuth2AuthorizationRequest}, or null if not found.
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("[LOG] Removing OAuth2 authorization request.");
        return this.loadAuthorizationRequest(request);
    }
}
