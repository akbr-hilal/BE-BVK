package id.co.bvk.management.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import id.co.bvk.management.models.User;
import id.co.bvk.management.repository.UserRepository;
import id.co.bvk.management.utils.JwtUtil;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 *
 * @author Akbr
 */
@Service
public class AuthService {

    @Autowired
    private UserService userService;

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

    public User loginWithGoogle(String token) {
        System.out.println("token: " + token);
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
//        // Mengambil email dan Google ID dari token
//        String[] tokenParts = token.split("\\."); // Memisahkan bagian token
//        System.out.println("tokenParts: " + tokenParts.length);
//        if (tokenParts.length < 2) {
//            return null; // Token tidak valid
//        }
//        // Decode payload menggunakan Base64 URL
//        String payload;
//        try {
//            System.out.println("tokenParts[1]: " + tokenParts[1]);
//            payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            return null; // Menangani kesalahan saat decoding
//        }
//        String payload = new String(java.util.Base64.getDecoder().decode(token)); // Mendapatkan payload
//        System.out.println("Payload: " + payload);
//        JsonObject jsonPayload = JsonParser.parseString(payload).getAsJsonObject();
////
//        String email = jsonPayload.get("email").getAsString();
//        String name = jsonPayload.get("name").getAsString();
//        String googleId = jsonPayload.get("sub").getAsString(); // Google ID
//
//        // Cek apakah user sudah terdaftar di database
//        return userService.findByEmail(email).orElseGet(() -> {
//            // Jika belum terdaftar, buat user baru
//            return userService.registerUser(email, null, name, googleId);
//        });
        // Ganti dengan CLIENT_ID yang sesuai
        String CLIENT_ID = "557922696929-17ah9ac32qtc0rgdgcgc5h14vhmqcc17.apps.googleusercontent.com";
//        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//        // Create a GooglePublicKeysManager instance
//        GooglePublicKeysManager googlePublicKeysManager;
//        try {
//            googlePublicKeysManager = new GooglePublicKeysManager(
//                    GoogleNetHttpTransport.newTrustedTransport(),
//                    jsonFactory
//            );
//        } catch (GeneralSecurityException | IOException e) {
//            e.printStackTrace();
//            return null; // Handle error
//        }
        // Create a NetHttpTransport instance
//        NetHttpTransport transport;
//        try {
//            transport = GoogleNetHttpTransport.newTrustedTransport(); // Using GoogleNetHttpTransport for HTTP
//        } catch (GeneralSecurityException | IOException e) {
//            e.printStackTrace();
//            return null; // Handle error
//        }
//        // Verifier untuk memverifikasi token
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(googlePublicKeysManager)
//                .setAudience(Collections.singletonList(CLIENT_ID))
//                .build();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

//
//        // Verifikasi token dan ambil payload
        GoogleIdToken idToken;
        String sanitizedToken = token.replaceAll("[^A-Za-z0-9-_\\.]", "");
        try {
            idToken = verifier.verify(sanitizedToken);
            if (idToken == null) {
                System.out.println("Invalid ID token.");
                return null; // Token tidak valid
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null; // Menangani kesalahan saat verifikasi
        }

        // Mengambil payload
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name"); // Mengambil nama
        String googleId = payload.getSubject(); // Google ID

        // Cek apakah user sudah terdaftar di database
        return userService.findByEmail(email).orElseGet(() -> {
            // Jika belum terdaftar, buat user baru
            return userService.registerUser(email, null, name, googleId);
        });
//        return null;
    }
}
