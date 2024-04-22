package taskflower.taskflower.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

@Slf4j
public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int expireSeconds) {
        log.info("[LOG - CookieUtils.addCookie]");
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expireSeconds);
        response.addCookie(cookie);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        log.info("[LOG - CookieUtils.getCookie]");
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        log.info("[LOG - CookieUtils.deleteCookie]");
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

    public static String serialize(Object obj) {
        log.info("[LOG - CookieUtils.serialize]");
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(String base64String, Class<T> cls) {
        log.info("[LOG - CookieUtils.deserialize]");
//        Serialization.deserialize deprecated 대응 필요
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(base64String)));
    }
}