package com.hanabank.model;

import javax.persistence.*;


@Entity
@Table(name="accountUsers")
public class Account {
    @Id
    @Column(name="id")
    @GeneratedValue
    private int id;

    @Column(name="email")
    private String email;

    @Column(name = "balance")
    private double balance;

    public Account() {
    }

    public Account(String email) {
        this.id = id;
        this.email = email;
        this.balance = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public double getBalance() {
        return balance;
    }

    public void setBalance(double addBalance) {
        this.balance = this.balance + addBalance;
    }
}


