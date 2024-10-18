package id.co.bvk.management.controllers;

import id.co.bvk.management.dto.LoginDto;
import id.co.bvk.management.dto.RegisterDto;
import id.co.bvk.management.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import id.co.bvk.management.services.AuthService;
import id.co.bvk.management.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Akbr
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    @GetMapping("/test")
    public String Test() {
        return "Auth Service successfully run";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        if(("".equals(dto.getEmail()) || dto.getEmail() == null) && ("".equals(dto.getPassword()) || dto.getPassword() == null)){
             return ResponseEntity.badRequest().body("Email and Password is Required");
        } else if ("".equals(dto.getEmail()) || dto.getEmail() == null){
             return ResponseEntity.badRequest().body("Email is Required");
        } else if ("".equals(dto.getPassword()) || dto.getPassword() == null){
             return ResponseEntity.badRequest().body("Password is Required");
        }
        userService.registerUser(dto.getEmail(), dto.getPassword(),dto.getName(), "");
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        if(("".equals(dto.getEmail()) || dto.getEmail() == null) && ("".equals(dto.getPassword()) || dto.getPassword() == null)){
             return ResponseEntity.badRequest().body("Email and Password is Required");
        } else if ("".equals(dto.getEmail()) || dto.getEmail() == null){
             return ResponseEntity.badRequest().body("Email is Required");
        } else if ("".equals(dto.getPassword()) || dto.getPassword() == null){
             return ResponseEntity.badRequest().body("Password is Required");
        }
        String token = authService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody String token) {
        User user = authService.loginWithGoogle(token);
        if (user != null) {
            return ResponseEntity.ok("User logged in: " + user.getEmail());
        } else {
            return ResponseEntity.badRequest().body("Invalid token or user not found.");
        }
    }

}
