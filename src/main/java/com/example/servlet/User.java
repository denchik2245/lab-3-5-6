package com.example.servlet;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "pass_hash", nullable = false)
    private String passHash;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "home_dir", nullable = false)
    private String homeDir;

    public User() {}

    public User(String username, String passHash, String email, String homeDir) {
        this.username = username;
        this.passHash = passHash;
        this.email = email;
        this.homeDir = homeDir;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassHash() { return passHash; }
    public void setPassHash(String passHash) { this.passHash = passHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHomeDir() { return homeDir; }
    public void setHomeDir(String homeDir) { this.homeDir = homeDir; }
}