package codearchitect99.taskory.security.token;

import codearchitect99.taskory.security.model.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Implementation of the TokenService interface that provides methods to create, validate, and parse JWT tokens.
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${app.token.expire-ms}")
    private long expireMS;

    @Value("${app.token.secret-key}")
    private String key;

    private SecretKey secretKey;

    /**
     * Initializes the secret key used for signing JWT tokens.
     * This method is automatically invoked after the bean construction.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("[LOG] Secret key initialized.");
    }

    /**
     * Creates a JWT token for the given authentication object.
     *
     * @param authentication the authentication object containing the authenticated user's details.
     * @return the generated JWT token as a String.
     */
    @Override
    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        log.info("[LOG] Creating token for user: {}", userPrincipal.getUsername());

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireMS);

        String token = Jwts.builder()
                .subject(Long.toString(userPrincipal.getId()))
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();

        log.info("[LOG] Token created: {}", token);
        return token;
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate.
     * @return true if the token is valid; false otherwise.
     */
    @Override
    public boolean isValidatedToken(String token) {
        log.info("[LOG] Validating token: {}", token);
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            log.info("[LOG] Token is valid.");
            return true;
        } catch (SignatureException exception) {
            log.error("[LOG] Invalid token signature.");
        } catch (MalformedJwtException exception) {
            log.error("[LOG] Invalid token format.");
        } catch (ExpiredJwtException exception) {
            log.error("[LOG] Token has expired.");
        } catch (UnsupportedJwtException exception) {
            log.error("[LOG] Token is unsupported.");
        } catch (IllegalArgumentException exception) {
            log.error("[LOG] Token claims are empty.");
        }
        return false;
    }

    /**
     * Extracts the user ID from the given JWT token.
     *
     * @param token the JWT token from which to extract the user ID.
     * @return the extracted user ID as a Long.
     */
    @Override
    public Long getUserIdFromToken(String token) {
        log.info("[LOG] Extracting user ID from token.");
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId = Long.parseLong(claims.getSubject());
        log.info("[LOG] Extracted user ID: {}", userId);
        return userId;
    }

    /**
     * Retrieves the JWT token from the HTTP request's Authorization header.
     *
     * @param request the HttpServletRequest from which to retrieve the token.
     * @return the JWT token as a String, or null if the token is not found or invalid.
     */
    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("[LOG] Extracting token from request header: {}", bearerToken);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info("[LOG] Extracted token: {}", token);
            return token;
        } else {
            log.warn("[LOG] No Bearer token found in the request.");
            return null;
        }
    }
}
