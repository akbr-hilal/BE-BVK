package id.co.bvk.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import id.co.bvk.management.models.User;
import id.co.bvk.management.repository.UserRepository;
import id.co.bvk.management.utils.JwtUtil;
import java.util.Optional;

/**
 *
 * @author Akbr
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public class AuthResponse {

        private final String token;
        private final String name;

        public AuthResponse(String token, String name) {
            this.token = token;
            this.name = name;
        }

        public String getToken() {
            return token;
        }

        public String getName() {
            return name;
        }
    }

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        
        if(user.getIsGoogleLogin()){
             throw new IllegalArgumentException("Please Login Using Google");
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getName());
            return new AuthResponse(token, user.getName());
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public AuthResponse loginWithGoogle(String email, String idGoogle) {
        User user = userRepository.findByGoogleIdAndEmail(idGoogle, email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Google"));
        String tokenJwt = jwtUtil.generateToken(user.getEmail(), user.getName());
        return new AuthResponse(tokenJwt, user.getName());
    }
}
