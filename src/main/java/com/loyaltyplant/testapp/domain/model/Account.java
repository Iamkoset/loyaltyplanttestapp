package com.loyaltyplant.testapp.domain.model;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountNumber;
    @ManyToOne
    private User user;
    @NotNull
    @Column(columnDefinition = "bigint default 0")
    private long currency;

    public Account() {
    }


    public long withdrawFunds(long amount) {
        currency -= amount;
        return currency;
    }

    public long addFunds(long amount) {
        currency += amount;
        return currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public long getCurrency() {
        return currency;
    }

    public void setCurrency(long currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", currency=" + currency +
                '}';
    }
}
