package com.bravos.steak.jwtauthentication.common.filter;

import com.bravos.steak.jwtauthentication.common.model.TokenClaims;
import com.bravos.steak.jwtauthentication.common.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String token = extractToken(request);

        if(requestURI.startsWith("auth/") || token == null || token.isBlank()) {
            filterChain.doFilter(request,response);
            return;
        }

        try {
            TokenClaims tokenClaims = jwtService.verifyTokenAndParse(token.trim());
            Authentication authentication = new AbstractAuthenticationToken(List.of(new SimpleGrantedAuthority("ROLE_USER"))) {
                @Override
                public Object getCredentials() {
                    return tokenClaims;
                }

                @Override
                public Object getPrincipal() {
                    return tokenClaims.getId();
                }
            };
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.warn("JWT verification failed: " + e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }

    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
