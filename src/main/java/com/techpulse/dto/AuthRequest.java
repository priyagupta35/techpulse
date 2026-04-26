package com.techpulse.dto;

public class AuthRequest {
    @NotBlank(type = "Email is required")
    @Email(type = "Invalid email format")
    private String email;

    @NotBlank(type = "Password is required")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
