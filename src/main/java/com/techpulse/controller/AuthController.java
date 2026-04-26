package com.techpulse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.techpulse.dto.AuthRequest;
import com.techpulse.dto.AuthResponse;
import com.techpulse.dto.RegisterRequest;
import com.techpulse.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    //amyone can register -no authentication required
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
       @Valid @RequestBody RegisterRequest request)
        {
             return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
public ResponseEntity<AuthResponse> login(
       @Valid @RequestBody AuthRequest request) {

    return ResponseEntity.ok(authService.login(request));
}

}
