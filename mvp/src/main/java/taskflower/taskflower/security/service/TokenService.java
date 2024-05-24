package taskflower.taskflower.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import taskflower.taskflower.security.model.UserPrincipal;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Value("${app.token.expire-ms}")
    private long expireMS;

    @Value("${app.token.secret-key}")
    private String key;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        log.info("[LOG - TokenProvider.createToken]");
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        log.info("[LOG - TokenProvider.createToken] userDetails : {}", userDetails.toString());

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireMS);

        return Jwts.builder()
                .subject(Long.toString(userDetails.getId()))
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validationToken(String jwt) {
        log.info("[LOG - TokenProvider.validationToken]");
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT");
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT");
        } catch (UnsupportedJwtException exception) {
            log.error("unsupported JWT");
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims starting is empty");
        }
        return false;
    }

    public long getUserIdFromToken(String jwt) {
        log.info("[LOG - TokenProvider.getUserIdFromToken]");
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        log.info("[LOG - TokenFilter.getFromRequest");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
