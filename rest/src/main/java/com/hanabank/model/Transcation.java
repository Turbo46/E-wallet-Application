package com.hanabank.model;

public class Transcation {

    private String email;
    private double balance;

    public Transcation(String email, double balance) {
        this.email = email;
        this.balance = balance;
    }

    public Transcation() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
