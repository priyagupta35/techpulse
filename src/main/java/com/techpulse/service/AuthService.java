package com.techpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techpulse.dto.AuthRequest;
import com.techpulse.dto.AuthResponse;
import com.techpulse.dto.RegisterRequest;
import com.techpulse.model.User;
import com.techpulse.repository.UserRepository;
import com.techpulse.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail())
                .isPresent()) {
            throw new RuntimeException(
                "Email already registered: " + request.getEmail());
        }

        // Default role to READER if not provided
        String role = (request.getRole() != null
            && !request.getRole().isBlank())
            ? request.getRole().toUpperCase()
            : "READER";

        User user = new User();
        user.setUsername(request.getUsername());
        // Fix — use request.getEmail() not email()
        user.setEmail(request.getEmail());
        user.setPassword(
            passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, role, "Registration successful");
    }

    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException(
                "User not found"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getRole(),
            "Login successful");
    }
}