package id.co.bvk.management.controllers;

import id.co.bvk.management.dto.LoginDto;
import id.co.bvk.management.dto.RegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import id.co.bvk.management.services.AuthService;
import id.co.bvk.management.services.UserService;
import id.co.bvk.management.utils.JwtUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author Akbr
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/test")
    public String Test() {
        return "Auth Service successfully run";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        System.out.println("isGoogleRegister: " + dto.getIsGoogle());
        if (dto.getIsGoogle()) {
            userService.registerUser(dto.getEmail(), "", dto.getName(), dto.getIdGoogle());
            return ResponseEntity.ok("User registered successfully");
        } else {
            if (("".equals(dto.getName()) || dto.getName() == null) && ("".equals(dto.getEmail()) || dto.getEmail() == null) && ("".equals(dto.getPassword()) || dto.getPassword() == null)) {
                return ResponseEntity.badRequest().body("Name, Email and Password is Required");
            } else if ("".equals(dto.getName()) || dto.getName() == null) {
                return ResponseEntity.badRequest().body("Name is Required");
            } else if ("".equals(dto.getEmail()) || dto.getEmail() == null) {
                return ResponseEntity.badRequest().body("Email is Required");
            } else if ("".equals(dto.getPassword()) || dto.getPassword() == null) {
                return ResponseEntity.badRequest().body("Password is Required");
            }
            userService.registerUser(dto.getEmail(), dto.getPassword(), dto.getName(), "");
            return ResponseEntity.ok("User registered successfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        System.out.println("isGoogle: " + dto.getIsGoogle());
        if (dto.getIsGoogle()) {
            AuthService.AuthResponse response = authService.loginWithGoogle(dto.getEmail(), dto.getIdGoogle());
            return ResponseEntity.ok(response);
        } else {
            if (("".equals(dto.getEmail()) || dto.getEmail() == null) && ("".equals(dto.getPassword()) || dto.getPassword() == null)) {
                return ResponseEntity.badRequest().body("Email and Password is Required");
            } else if ("".equals(dto.getEmail()) || dto.getEmail() == null) {
                return ResponseEntity.badRequest().body("Email is Required");
            } else if ("".equals(dto.getPassword()) || dto.getPassword() == null) {
                return ResponseEntity.badRequest().body("Password is Required");
            }
            AuthService.AuthResponse response = authService.login(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String token) {
        try {
            // Menghapus "Bearer " dari token
            String cleanToken = token.replace("Bearer ", "");

            // Mengekstrak email atau informasi lain dari token
            String email = jwtUtil.extractEmail(cleanToken);
            String name = jwtUtil.extractName(cleanToken);
            
            // Memvalidasi token
            if (email != null && jwtUtil.isTokenValid(cleanToken, email)) {
                return ResponseEntity.ok(Map.of("email", email, "name", name, "message", "Token is valid."));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
}
