package com.techpulse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="username",nullable=false)
    private String username;

    @Column(name="email",unique=true,nullable=false)
    private String email;
  
    @Column(name="password",nullable=false)
    private String password;

    @Column(name="role",columnDefinition="ENUM('READER','CONTRIBUTOR','ADMIN')")
    private  String role;

    
    public User() {
        
    }

    public User(String username,String email,String password,String role){
       this.username=username;
       this.email=email;
       this.password=password;
       this.role=role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
