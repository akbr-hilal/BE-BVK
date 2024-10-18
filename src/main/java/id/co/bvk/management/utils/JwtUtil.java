package id.co.bvk.management.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

/**
 *
 * @author Akbr
 */
@Component
public class JwtUtil {

    // Anda bisa menyimpan kunci secara aman atau menghasilkan setiap kali, sesuai kebutuhan
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 256-bit key

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 jam expiry
                .signWith(SECRET_KEY) // Menggunakan kunci yang benar dengan ukuran yang aman
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Menggunakan kunci yang sama untuk verifikasi
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
