package uniquindio.product.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // === PÚBLICO ===
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/publico/**",
                                "/api/pagos/notificacion",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // === CLIENTE ===
                        .requestMatchers("/api/usuarios/**").hasRole("CLIENTE")
                        .requestMatchers("/api/pedidos/**").hasRole("CLIENTE")
                        .requestMatchers("/api/carrito/**").hasRole("CLIENTE") // ← Si existe

                        // === ADMINISTRADOR (acceso total) ===
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")

                        // === GESTOR PRODUCTOS ===
                        .requestMatchers(HttpMethod.GET, "/api/productos/**")
                        .hasAnyRole("GESTOR_PRODUCTOS", "SUPERVISOR_PRODUCCION", "ENCARGADO_ALMACEN", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/productos/**")
                        .hasAnyRole("GESTOR_PRODUCTOS", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**")
                        .hasAnyRole("GESTOR_PRODUCTOS", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**")
                        .hasAnyRole("GESTOR_PRODUCTOS", "ADMINISTRADOR")

                        // === SUPERVISOR PRODUCCIÓN ===
                        .requestMatchers(HttpMethod.GET, "/api/lotes/**")
                        .hasAnyRole("SUPERVISOR_PRODUCCION", "ENCARGADO_ALMACEN", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/lotes/**")
                        .hasAnyRole("SUPERVISOR_PRODUCCION", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/lotes/**")
                        .hasAnyRole("SUPERVISOR_PRODUCCION", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/lotes/**")
                        .hasAnyRole("SUPERVISOR_PRODUCCION", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/lotes/**")
                        .hasAnyRole("SUPERVISOR_PRODUCCION", "ADMINISTRADOR")

                        // === ENCARGADO ALMACÉN ===
                        .requestMatchers(HttpMethod.GET, "/api/inventario/**")
                        .hasAnyRole("ENCARGADO_ALMACEN", "SUPERVISOR_PRODUCCION", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/inventario/registrar-entrada/**")
                        .hasAnyRole("ENCARGADO_ALMACEN", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/inventario/**")
                        .hasAnyRole("ENCARGADO_ALMACEN", "ADMINISTRADOR")

                        // === DEFAULT ===
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://renechardon.vercel.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}