package taskflower.taskflower.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenProvider {

    @Value("${app.token.expireMSec}")
    private String expireMSec;

    @Value("${app.token.secretKey}")
    private String key;

    private SecretKey secretKey() {
        byte[] keyBytes = Decoders.BASE64.decode( key);
        return Keys.hmacShaKeyFor(keyBytes);
    };


    public String createToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireMSec);


        return Jwts.builder()
                .subject(Long.toString(userDetails.getId()))
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey())
                .compact();
    }

}
