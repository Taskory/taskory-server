package codeartitect.taskflower.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public TokenFilter(TokenService tokenService, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenService.getTokenFromRequest(request);
        if (StringUtils.hasText(token) && tokenService.isValidatedToken(token)) {
            Long userId = tokenService.getUserIdFromToken(token);

            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUserId(userId);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal, userPrincipal.getPassword(), userPrincipal.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        }
    }
}
