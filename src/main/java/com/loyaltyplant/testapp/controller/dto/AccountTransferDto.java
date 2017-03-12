package com.loyaltyplant.testapp.controller.dto;

public class AccountTransferDto {

    private long fromAccount;
    private long toAccount;
    private long amount;
    private long senderResultCurrency;
    private long recipientResultCurrency;

    public AccountTransferDto() {
    }

    public long getSenderResultCurrency() {
        return senderResultCurrency;
    }

    public void setSenderResultCurrency(long senderResultCurrency) {
        this.senderResultCurrency = senderResultCurrency;
    }

    public long getRecipientResultCurrency() {
        return recipientResultCurrency;
    }

    public void setRecipientResultCurrency(long recipientResultCurrency) {
        this.recipientResultCurrency = recipientResultCurrency;
    }

    public long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public long getToAccount() {
        return toAccount;
    }

    public void setToAccount(long toAccount) {
        this.toAccount = toAccount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
