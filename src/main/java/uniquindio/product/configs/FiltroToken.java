package uniquindio.product.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.model.enums.Rol;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FiltroToken extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String token = getToken(request);
        boolean error = true;

        try {
            if (requestURI.startsWith("/api/cuenta")) {
                error = validarToken(token, Rol.CLIENTE);
            } else if (requestURI.startsWith("/api/admin")) {
                error = validarToken(token, Rol.ADMINISTRADOR);
            } else {
                error = false;
            }

            if (error) {
                crearRespuestaError("No tiene permisos para acceder a este recurso", HttpServletResponse.SC_FORBIDDEN, response);
                return; // ✅ importante detener la cadena
            }

            filterChain.doFilter(request, response);

        } catch (MalformedJwtException | SignatureException e) {
            crearRespuestaError("El token es incorrecto", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        } catch (ExpiredJwtException e) {
            crearRespuestaError("El token está vencido", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        } catch (Exception e) {
            crearRespuestaError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
    }

    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.replace("Bearer ", "") : null;
    }

    private void crearRespuestaError(String mensaje, int codigoError, HttpServletResponse response) throws IOException {
        MensajeDTO<String> dto = new MensajeDTO<>(true, mensaje);

        response.setContentType("application/json");
        response.setStatus(codigoError);
        response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
        response.getWriter().flush();
        response.getWriter().close();
    }

    private boolean validarToken(String token, Rol rol){
        boolean error = true;
        if (token != null) {
            Jws<Claims> jws = JWTUtils.parseJwt(token);
            Claims claims = jws.getBody(); // ✅ antes era getPayload()
            if (Rol.valueOf(claims.get("rol").toString()) == rol) {
                error = false;
            }
        }
        return error;
    }
}