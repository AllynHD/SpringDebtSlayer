package com.example.SpringDebtSlayer.Models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max = 20, message = "Username must have length of 3-20 characters")
    @Column(unique = true)
    private String username;

    @NotNull
    private String pwHash;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @OneToMany
    @JoinColumn(name="user_id")
    private List<Debt> debts = new ArrayList<>();

    private int months;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.pwHash = hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private static String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public int getId() {        //setter for Id?
        return id;
    }

    public List<Debt> getDebts() {     //setter for debtList?
        return debts;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }
}
