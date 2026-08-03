package com.bank.user;

import com.bank.role.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final UUID userID;
    private final String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Character middleInitial;
    private Role role;
    private final LocalDateTime createdAt;

    public User(String username, String firstName, String lastName, Character middleInitial, String passwordHash, Role role) {
        this.userID = UUID.randomUUID();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public Character getMiddleInitial() { return this.middleInitial; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public Role getRole() { return this.role; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setMiddleInitial(Character middleInitial) { this.middleInitial = middleInitial; }
    public void setRole(Role role) { this.role = role; }
    public UUID getUserID() { return this.userID; }
    public String getPasswordHash() { return this.passwordHash; }
    public String getUsername() { return this.username; }
    public void setPasswordHash(String newHash) { this.passwordHash = newHash; }
}
