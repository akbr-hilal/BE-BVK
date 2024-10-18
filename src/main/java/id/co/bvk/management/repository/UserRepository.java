package id.co.bvk.management.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import id.co.bvk.management.models.User;

/**
 *
 * @author Akbr
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);
    
    Optional<User> findByGoogleIdAndEmail(String googleId, String email);
}