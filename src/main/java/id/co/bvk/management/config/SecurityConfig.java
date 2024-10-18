package id.co.bvk.management.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Gunakan HttpSecurity untuk mengatur otorisasi
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Menggunakan CORS configuration
                .authorizeHttpRequests(authorizeRequests
                        -> authorizeRequests
                        .anyRequest().permitAll() // Izinkan semua permintaan tanpa otentikasi
                )
                .csrf(csrf -> csrf.disable()) // Nonaktifkan CSRF untuk endpoint publik jika diperlukan
                .httpBasic(Customizer.withDefaults()) // Tambahkan otentikasi dasar jika diperlukan
                .formLogin(form -> form.disable());  // Nonaktifkan form login;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Ganti dengan origin yang diizinkan
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Metode HTTP yang diizinkan
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Header yang diizinkan
        configuration.setAllowCredentials(true); // Izinkan pengiriman credential
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Daftarkan konfigurasi CORS untuk semua endpoint
        return source;
    }
}
