package codeartist99.taskflower.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

/**
 * Utility class for managing HTTP cookies.
 * Provides methods to add, retrieve, delete cookies, and serialize/deserialize objects.
 */
@Slf4j
public class CookieUtil {

    /**
     * Adds a cookie to the HTTP response with the specified name, value, and expiration time.
     *
     * @param response the {@link HttpServletResponse} to which the cookie is added.
     * @param cookieName the name of the cookie.
     * @param value the value to store in the cookie.
     * @param expireSeconds the expiration time in seconds.
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String value, int expireSeconds) {
        log.info("[LOG] Adding cookie: {} with expiration: {} seconds", cookieName, expireSeconds);

        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expireSeconds);
        response.addCookie(cookie);

        log.info("[LOG] Cookie added: {}", cookieName);
    }

    /**
     * Retrieves a cookie from the HTTP request by its name.
     *
     * @param request the {@link HttpServletRequest} containing the cookies.
     * @param cookieName the name of the cookie to retrieve.
     * @return an {@link Optional} containing the cookie if found, or an empty {@link Optional} if not found.
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        log.info("[LOG] Retrieving cookie: {}", cookieName);

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("[LOG] No cookies found in the request.");
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                log.info("[LOG] Cookie found: {}", cookieName);
                return Optional.of(cookie);
            }
        }

        log.warn("[LOG] Cookie not found: {}", cookieName);
        return Optional.empty();
    }

    /**
     * Deletes a cookie from the HTTP request and response by setting its value to an empty string
     * and its expiration time to 0.
     *
     * @param request the {@link HttpServletRequest} containing the cookies.
     * @param response the {@link HttpServletResponse} to update with the deleted cookie.
     * @param cookieName the name of the cookie to delete.
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        log.info("[LOG] Deleting cookie: {}", cookieName);

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("[LOG] No cookies available to delete.");
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                cookie.setPath("/");
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

                log.info("[LOG] Cookie deleted: {}", cookieName);
            }
        }
    }

    /**
     * Serializes an object to a Base64-encoded string for storage in cookies.
     *
     * @param object the object to serialize.
     * @return the serialized object as a Base64-encoded string.
     */
    public static String serialize(Object object) {
        log.info("[LOG] Serializing object of type: {}", object.getClass().getSimpleName());

        String serialized = Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
        log.info("[LOG] Serialization completed.");
        return serialized;
    }

    /**
     * Deserializes a Base64-encoded string into an object of the specified class type.
     *
     * @param value the Base64-encoded string to deserialize.
     * @param cls the class type to deserialize into.
     * @param <T> the type of the class to return.
     * @return the deserialized object of the specified type.
     */
    public static <T> T deserialize(String value, Class<T> cls) {
        log.info("[LOG] Deserializing to class: {}", cls.getSimpleName());

        T deserializedObject = cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(value)));
        log.info("[LOG] Deserialization completed.");
        return deserializedObject;
    }
}
