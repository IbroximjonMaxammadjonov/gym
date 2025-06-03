package com.ibroximjon.gym.dto;

import com.ibroximjon.gym.model.User;


public class RegisterRequestDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;


    public User toUser() {
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setActive(this.isActive);
        return user;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
