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

    // ============================================
    // CONSTANTES DE ROLES
    // ============================================
    private static final String ROL_CLIENTE = "CLIENTE";
    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String ROL_GESTOR_PRODUCTOS = "GESTOR_PRODUCTOS";
    private static final String ROL_SUPERVISOR_PRODUCCION = "SUPERVISOR_PRODUCCION";
    private static final String ROL_ENCARGADO_ALMACEN = "ENCARGADO_ALMACEN";

    // ============================================
    // CONSTANTES DE ENDPOINTS PÚBLICOS
    // ============================================
    private static final String[] ENDPOINTS_PUBLICOS = {
            "/api/auth/**",
            "/api/publico/**",
            "/api/pagos/notificacion",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    // ============================================
    // CONSTANTES DE CORS
    // ============================================
    private static final String ORIGEN_LOCAL = "http://localhost:4200";
    private static final String ORIGEN_VERCEL = "https://renechardon.vercel.app";
    private static final String ORIGEN_DOMINIO_PROPIO = "https://essencial.ddns.net";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ============================================
    // CODIFICADOR DE CONTRASEÑAS
    // ============================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ============================================
    // CADENA DE FILTROS DE SEGURIDAD
    // ============================================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sesion ->
                        sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(this::configurarAutorizaciones)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ============================================
    // CONFIGURACIÓN DE AUTORIZACIONES
    // ============================================
    private void configurarAutorizaciones(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {

        // === ENDPOINTS PÚBLICOS ===
        auth.requestMatchers(ENDPOINTS_PUBLICOS).permitAll();

        // === CLIENTE ===
        configurarAccesoCliente(auth);

        // === ADMINISTRADOR (acceso total a endpoints administrativos) ===
        auth.requestMatchers("/api/admin/**").hasRole(ROL_ADMINISTRADOR);

        // === GESTOR DE PRODUCTOS ===
        configurarAccesoGestorProductos(auth);

        // === SUPERVISOR DE PRODUCCIÓN ===
        configurarAccesoSupervisorProduccion(auth);

        // === ENCARGADO DE ALMACÉN ===
        configurarAccesoEncargadoAlmacen(auth);

        // === CUALQUIER OTRO ENDPOINT REQUIERE AUTENTICACIÓN ===
        auth.anyRequest().authenticated();
    }

    // ============================================
    // ACCESO CLIENTE
    // ============================================
    private void configurarAccesoCliente(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {

        auth.requestMatchers("/api/usuarios/**").hasRole(ROL_CLIENTE);
        auth.requestMatchers("/api/pedidos/**").hasRole(ROL_CLIENTE);
        auth.requestMatchers("/api/carrito/**").hasRole(ROL_CLIENTE);
    }

    // ============================================
    // ACCESO GESTOR DE PRODUCTOS
    // ============================================
    private void configurarAccesoGestorProductos(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {

        // Lectura: accesible también por Supervisor y Encargado
        auth.requestMatchers(HttpMethod.GET, "/api/productos/**")
                .hasAnyRole(
                        ROL_GESTOR_PRODUCTOS,
                        ROL_SUPERVISOR_PRODUCCION,
                        ROL_ENCARGADO_ALMACEN,
                        ROL_ADMINISTRADOR
                );

        // Escritura: solo Gestor y Admin
        auth.requestMatchers(HttpMethod.POST, "/api/productos/**")
                .hasAnyRole(ROL_GESTOR_PRODUCTOS, ROL_ADMINISTRADOR);

        auth.requestMatchers(HttpMethod.PUT, "/api/productos/**")
                .hasAnyRole(ROL_GESTOR_PRODUCTOS, ROL_ADMINISTRADOR);

        auth.requestMatchers(HttpMethod.DELETE, "/api/productos/**")
                .hasAnyRole(ROL_GESTOR_PRODUCTOS, ROL_ADMINISTRADOR);
    }

    // ============================================
    // ACCESO SUPERVISOR DE PRODUCCIÓN
    // ============================================
    private void configurarAccesoSupervisorProduccion(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {

        // Lectura: accesible también por Encargado
        auth.requestMatchers(HttpMethod.GET, "/api/lotes/**")
                .hasAnyRole(
                        ROL_SUPERVISOR_PRODUCCION,
                        ROL_ENCARGADO_ALMACEN,
                        ROL_ADMINISTRADOR
                );

        // Escritura: solo Supervisor y Admin
        auth.requestMatchers(HttpMethod.POST, "/api/lotes/**")
                .hasAnyRole(ROL_SUPERVISOR_PRODUCCION, ROL_ADMINISTRADOR);

        auth.requestMatchers(HttpMethod.PUT, "/api/lotes/**")
                .hasAnyRole(ROL_SUPERVISOR_PRODUCCION, ROL_ADMINISTRADOR);

        auth.requestMatchers(HttpMethod.PATCH, "/api/lotes/**")
                .hasAnyRole(ROL_SUPERVISOR_PRODUCCION, ROL_ADMINISTRADOR);

        auth.requestMatchers(HttpMethod.DELETE, "/api/lotes/**")
                .hasAnyRole(ROL_SUPERVISOR_PRODUCCION, ROL_ADMINISTRADOR);
    }

    // ============================================
    // ACCESO ENCARGADO DE ALMACÉN
    // ============================================
    private void configurarAccesoEncargadoAlmacen(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {

        // Lectura: accesible también por Supervisor
        auth.requestMatchers(HttpMethod.GET, "/api/inventario/**")
                .hasAnyRole(
                        ROL_ENCARGADO_ALMACEN,
                        ROL_SUPERVISOR_PRODUCCION,
                        ROL_ADMINISTRADOR
                );

        // Operaciones específicas de almacén
        auth.requestMatchers(HttpMethod.POST, "/api/inventario/registrar-entrada/**")
                .hasAnyRole(ROL_ENCARGADO_ALMACEN, ROL_ADMINISTRADOR);

        auth.requestMatchers(HttpMethod.PUT, "/api/inventario/**")
                .hasAnyRole(ROL_ENCARGADO_ALMACEN, ROL_ADMINISTRADOR);
    }

    // ============================================
    // CONFIGURACIÓN DE CORS
    // ============================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();

        // Orígenes permitidos (incluye tu nuevo dominio HTTPS)
        configuracion.setAllowedOrigins(List.of(
                ORIGEN_LOCAL,
                ORIGEN_VERCEL,
                ORIGEN_DOMINIO_PROPIO  // ← Tu dominio con SSL
        ));

        // Métodos HTTP permitidos
        configuracion.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Cabeceras permitidas (más amplio para evitar problemas)
        configuracion.setAllowedHeaders(List.of("*"));

        // Cabeceras expuestas al cliente
        configuracion.setExposedHeaders(List.of("Authorization"));

        // Permite credenciales (cookies, headers de autorización)
        configuracion.setAllowCredentials(true);

        // Tiempo de cacheo de preflight (OPTIONS)
        configuracion.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);

        return fuente;
    }
}
