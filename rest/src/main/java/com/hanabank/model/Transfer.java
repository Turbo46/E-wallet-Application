package com.hanabank.model;

public class Transfer {
    private String emailSource;
    private double balanceSource;
    private String emailTarget;

    public Transfer() {
    }

    public String getEmailSource() {
        return emailSource;
    }

    public void setEmailSource(String emailSource) {
        this.emailSource = emailSource;
    }

    public double getBalanceSource() {
        return balanceSource;
    }

    public void setBalanceSource(double balanceSource) {
        this.balanceSource = balanceSource;
    }

    public String getEmailTarget() {
        return emailTarget;
    }

    public void setEmailTarget(String emailTarget) {
        this.emailTarget = emailTarget;
    }
}
