package com.example.SpringDebtSlayer.Models.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginForm {

    @NotNull
    @Size(min=3, max = 20, message = "Username must have length of 3-20 characters")
    private String username;

    @NotNull
    @Size(min=6, message = "Password must contain at least 6 characters")
    private String password;

    public LoginForm() {}

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
}
