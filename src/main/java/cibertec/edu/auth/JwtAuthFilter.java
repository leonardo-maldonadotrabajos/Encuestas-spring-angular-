package cibertec.edu.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si no hay token, pasamos al siguiente filtro (Spring decidirá si es un endpoint público)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.esValido(token)) {
                UUID usuarioId = jwtUtil.extraerUsuarioId(token);
                String email = jwtUtil.extraerEmail(token);
                String rol = jwtUtil.extraerRol(token);

                var authority1 = new SimpleGrantedAuthority(rol);
                var authority2 = new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase());
                List<SimpleGrantedAuthority> authorities = List.of(authority1, authority2);

                var authentication = new UsernamePasswordAuthenticationToken(
                        new UsuarioPrincipal(usuarioId, email, rol),
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Token inválido: simplemente no autenticamos, la petición continúa
            System.err.println("Error procesando JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
