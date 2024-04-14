package taskflower.taskflower.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import taskflower.taskflower.security.local.CustomUserDetailsService;
import taskflower.taskflower.user.UserService;

import java.io.IOException;

@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    public TokenFilter(TokenService tokenService, CustomUserDetailsService userDetailsService, UserService userService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("[LOG - TokenFilter.doFilterInternal]");
        try {
            String jwt = getFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenService.validationToken(jwt)) {
                long userId = tokenService.getUserIdFromToken(jwt);

//                로컬 서비스 계정 가입 x, 로컬 계정 가입 요청 x 일 때,
                if (!isLocalSignup(userId) && !request.getRequestURI().equalsIgnoreCase("/api/v1/auth/oauth2/signup")) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    return;
                }

                UserDetails userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception exception) {
            log.error("Could not set user authentication in security context", exception);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLocalSignup(long userId) {
        return userService.getUserById(userId).isLocalSignup();
    }

    private String getFromRequest(HttpServletRequest request) {
        log.info("[LOG - TokenFilter.getFromRequest");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
