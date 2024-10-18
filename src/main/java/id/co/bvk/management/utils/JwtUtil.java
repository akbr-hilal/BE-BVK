package id.co.bvk.management.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author Akbr
 */
@Component
public class JwtUtil {

    private final String SECRET_KEY = "BVKMangementTestHilal1!";
    
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiry
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
