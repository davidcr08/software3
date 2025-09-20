package uniquindio.product.configs;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
@Setter
public class JWTUtils {

    private String secret;
    private Long expiration;
    public String generarToken(String email, Map<String, Object> claims) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1L, ChronoUnit.HOURS)))
                .signWith(getKey())
                .compact();
    }

    public Jws<Claims> parseJwt(String jwtString)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build();

        return jwtParser.parseClaimsJws(jwtString);
    }

    private SecretKey getKey() {
        byte[] secretKeyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }
}