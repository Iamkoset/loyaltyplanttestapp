package com.loyaltyplant.testapp.exceptions;

/**
 * Exception thrown if {@link com.loyaltyplant.testapp.domain.model.Account}
 * doesn't have valid currency for operations
 *
 * */
public class NotEnoughFundsException extends Exception {

    public NotEnoughFundsException(String message) {
        super(message);
    }
}
