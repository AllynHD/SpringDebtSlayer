package com.example.SpringDebtSlayer.Models.forms;

import javax.validation.constraints.NotNull;

public class RegisterForm extends LoginForm {
    @NotNull(message = "Passwords didn't match")
    private String verifyPassword;

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        checkPasswordForRegistration();
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
        checkPasswordForRegistration();
    }

    private void checkPasswordForRegistration() {
        if (!getPassword().equals(verifyPassword)) {
            verifyPassword = null;
        }
    }
}
