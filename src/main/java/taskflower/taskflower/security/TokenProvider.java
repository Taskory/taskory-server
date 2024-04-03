package taskflower.taskflower.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class TokenProvider {

    @Value("${app.token.expireMSec}")
    private long expireMSec;

    private SecretKey secretKey;

    public TokenProvider(@Value("${app.token.secretKey}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    public String createToken(Authentication authentication) {
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireMSec);

        return Jwts.builder()
                .subject(Long.toString(userDetails.getId()))
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validationToken(String jwt) {
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
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }
}
