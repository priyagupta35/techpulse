package com.techpulse.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter  extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;
   
    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException,IOException {

            final String authHeader=request.getHeader("Authorization");
            String email=null;
            String jwt=null;

          
            if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            jwt=authHeader.substring(7);
            try {
                email=jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT expired");
                } catch (JwtException e) {
                  logger.warn("Invalid JWT");
         }
            }
             
            if (email != null && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            // Load full user details from database
            UserDetails userDetails = userDetailsService
                .loadUserByUsername(email);

            // Validate the token against the loaded user
            if (jwt!=null && jwtUtil.validateToken(jwt, userDetails)) {
                // Create authentication token and set it in
                // Spring Security context — from this point
                // Spring knows who this request is from
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext()
                    .setAuthentication(authToken);
            }
        }

        // Continue the filter chain regardless
        filterChain.doFilter(request, response);
    }
}
