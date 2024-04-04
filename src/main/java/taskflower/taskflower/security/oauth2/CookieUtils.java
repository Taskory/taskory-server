package taskflower.taskflower.security.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void addCookie(HttpServletResponse response, String name, String value, int expireSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expireSeconds);
        response.addCookie(cookie);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                cookie.setPath("");
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(OAuth2AuthorizationRequest authorizationRequest) {
        try {
            String jsonString = objectMapper.writeValueAsString(authorizationRequest);
            return Base64.getUrlEncoder().encodeToString(jsonString.getBytes());
        } catch (JsonProcessingException e) {
            return null; // 또는 다른 기본값 또는 예외 발생을 나타내는 특정 문자열
        }
    }

    public static OAuth2AuthorizationRequest deserialize(Cookie cookie, Class<OAuth2AuthorizationRequest> oAuth2AuthorizationRequestClass) {
        try {
            byte[] decodeBytes = Base64.getUrlDecoder().decode(cookie.getValue());
            String jsonString = new String(decodeBytes);
            return objectMapper.readValue(jsonString, oAuth2AuthorizationRequestClass);
        } catch (JsonProcessingException e) {
            return null; // 또는 다른 기본값 또는 예외 발생을 나타내는 특정 값
        }
    }

}
