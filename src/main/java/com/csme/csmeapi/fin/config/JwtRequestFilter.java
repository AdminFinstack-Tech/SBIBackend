package com.csme.csmeapi.fin.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${custom.security.enabled}")
    private boolean securityEnabled;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (securityEnabled) {
                processToken(request,response);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        } catch (Exception ex) {
            handleTokenProcessingError(response, ex);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void processToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            String token = authHeader.substring(TOKEN_PREFIX.length());
            String userId = jwtTokenUtil.getUserIdFromToken(token);
            String unitCode = jwtTokenUtil.getUnitCodeFromToken(token);
            String refreshedToken = jwtTokenUtil.extendTokenExpiration(token);
            // Set the new token in the response header
            response.setHeader(AUTH_HEADER, TOKEN_PREFIX + refreshedToken);
            if (userId != null && unitCode != null && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtTokenUtil.validateToken(token)) {
                List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userId, "", authorities));
            }
        }
    }

    private void handleTokenProcessingError(HttpServletResponse response, Exception ex) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token processing error: " + ex.getMessage());
    }
}
