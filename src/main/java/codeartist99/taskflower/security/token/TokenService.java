package codeartist99.taskflower.security.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TokenService {
    String createToken(Authentication authentication);

    boolean isValidatedToken(String token);

    Long getUserIdFromToken(String token);

    String getTokenFromRequest(HttpServletRequest request);

}
