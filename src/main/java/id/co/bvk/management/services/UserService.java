package id.co.bvk.management.services;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import id.co.bvk.management.models.User;
import id.co.bvk.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Akbr
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String email, String password, String name, String googleId) {
        User user = new User();
        user.setEmail(email);
        if (googleId != null || !"".equals(googleId)) {
            user.setGoogleId(googleId);
            user.setIsGoogleLogin(true);
        } else {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setName(name);
        userRepository.save(user);
        userRepository.flush();
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }
}
