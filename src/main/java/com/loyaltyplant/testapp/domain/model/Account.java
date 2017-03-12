package com.loyaltyplant.testapp.domain.model;

import javax.persistence.*;

;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountNumber;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "bigint default 0", nullable = false)
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

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
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
