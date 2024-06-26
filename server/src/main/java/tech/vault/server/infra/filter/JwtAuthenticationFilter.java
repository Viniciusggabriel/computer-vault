package tech.vault.server.infra.filter;

import com.nimbusds.jose.util.StandardCharset;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.vault.server.infra.exception.ExJwtExpired;
import tech.vault.server.application.service.security.JwtService;

import java.io.IOException;

/**
 * Filtro para autenticação JWT.
 *
 * <p>Esta classe estende {@link OncePerRequestFilter}, uma classe abstrata para realizar a filtragem de um token JWT por requisição.</p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Realiza a filtragem de uma requisição HTTP buscando uma autenticação JWT.
     *
     * @param request     A requisição HTTP recebida.
     * @param response    A resposta HTTP a ser enviada.
     * @param filterChain A cadeia de filtros a ser utilizada.
     * @throws ServletException Se ocorrer um erro de servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //INFO: Seta o response como UTF8 e verifica o header da requisição
        response.setCharacterEncoding(StandardCharset.UTF_8.displayName());
        final String authHeader = request.getHeader("Authorization");

        //INFO: Verifica se o token está no header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //INFO: Remove o "Bearer" do jwt
        final String jwt = authHeader.substring(7);
        final String userName;

        //INFO: Tratamento de erros para um token invalido
        try {
            userName = jwtService.extractUserName(jwt);

        } catch (ExJwtExpired exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(exception.getJwtExpired());
            return;

        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("O token é invalido");
            return;

        }

        //INFO: Valida o usuário e se o token foi assinado corretamente
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            if (jwtService.isValidToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}