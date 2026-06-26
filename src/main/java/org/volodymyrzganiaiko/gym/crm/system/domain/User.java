package org.volodymyrzganiaiko.gym.crm.system.domain;

import java.util.UUID;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
    private UUID userId;

    public User() {
    }

    public User(String firstName, String lastName, String username, String password, boolean isActive, UUID userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.userId = userId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UUID getUserId() { return userId; }

    public void setUserId(UUID userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", isActive=" + isActive +
                ", userId=" + userId +
                '}';
    }
}
