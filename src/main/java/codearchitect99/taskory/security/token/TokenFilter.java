package codearchitect99.taskory.security.token;

import codearchitect99.taskory.security.model.UserPrincipal;
import codearchitect99.taskory.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that processes every request to validate JWT tokens.
 * If a valid token is found, it sets the authentication in the SecurityContext.
 */
@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructs the TokenFilter with dependencies injected via constructor.
     *
     * @param tokenService the service responsible for token operations (e.g., validation, parsing).
     * @param userDetailsService the service to load user details by ID.
     */
    @Autowired
    public TokenFilter(TokenService tokenService, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests, extracts the token, and sets authentication if the token is valid.
     *
     * @param request the HttpServletRequest object representing the incoming request.
     * @param response the HttpServletResponse object to modify the response.
     * @param filterChain the filter chain to pass the request/response to the next filter.
     * @throws ServletException if an error occurs during request processing.
     * @throws IOException if an I/O error occurs during request processing.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("[LOG] Request received at TokenFilter");

        String token = tokenService.getTokenFromRequest(request);
        log.info("[LOG] Extracted token: {}", token);

        if (StringUtils.hasText(token)) {
            if (tokenService.isValidatedToken(token)) {
                log.info("[LOG] Valid token: {}", token);

                Long userId = tokenService.getUserIdFromToken(token);
                log.info("[LOG] Extracted userId from token: {}", userId);

                UserPrincipal userPrincipal =
                        (UserPrincipal) userDetailsService.loadUserByUserId(userId);
                log.info("[LOG] Loaded user details: {}", userPrincipal.getUsername());
                log.info("[LOG] Loaded user authority: {}", userPrincipal.getAuthorities());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userPrincipal, null, userPrincipal.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("[LOG] Authentication set in SecurityContext");
            } else {
                log.warn("[LOG] Invalid token: {}", token);
            }
        } else {
            log.info("[LOG] No token found in the request");
        }

        filterChain.doFilter(request, response);
        log.info("[LOG] Request passed through TokenFilter");
    }
}
