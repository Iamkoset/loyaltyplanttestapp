package com.loyaltyplant.testapp.exceptions;

public class AccountDoesntExistException extends Exception {

    public AccountDoesntExistException(String message) {
        super(message);
    }
}
