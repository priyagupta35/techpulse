package com.techpulse.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        private Key getSigningKey(){
            return Keys.hmacShaKeyFor(secret.getBytes());
        }

        //generates a jwt toekn for a succesfully authorized user
        //the token contains the username-email and expiry time
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();

        claims.put("role",userDetails.getAuthorities()
        .stream()
        .map(a -> a.getAuthority())
        .toList());

        return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername()) // email
        .setExpiration(new Date(
            System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
   
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts the expiration date from a JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim from the token
    public <T> T extractClaim(String token,
            Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parses and returns all claims from a JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // Checks if token has passed its expiration time
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validates a token against the user it claims to represent
    // Checks both that the username matches and token is not expired
    public boolean validateToken(String token,
            UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
            && !isTokenExpired(token));
    }
}




