package uniquindio.product.configs;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtils {

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

    public static Jws<Claims> parseJwt(String jwtString)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build();

        return jwtParser.parseClaimsJws(jwtString);
    }

    private static SecretKey getKey() {
        String claveSecreta = "k8A9sFh3Jz7pQ4T1xVbWmR6LkNvU2gC5Yt0qXsEj8PdGhZw1l9Bn3yM2fX0rJkH7";
        byte[] secretKeyBytes = claveSecreta.getBytes();
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }
}