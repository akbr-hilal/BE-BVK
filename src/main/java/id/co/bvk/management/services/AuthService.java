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
    
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // private final String GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID";
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
    
    public String loginWithGoogle(String email, String idGoogle) {
        User user = userRepository.findByGoogleIdAndEmail(idGoogle, email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Google"));
        return jwtUtil.generateToken(user.getEmail());        
    }
}
