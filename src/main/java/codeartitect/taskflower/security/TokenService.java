package codeartitect.taskflower.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TokenService {
    String createToken(Authentication authentication);

    boolean isValidatedToken(String token);

    Long getUserIdFromToken(String token);

    String getTokenFromRequest(HttpServletRequest request);

}
