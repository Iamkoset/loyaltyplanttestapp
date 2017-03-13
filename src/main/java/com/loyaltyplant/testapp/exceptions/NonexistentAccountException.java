package com.loyaltyplant.testapp.exceptions;

/**
 * Exception thrown if {@link com.loyaltyplant.testapp.domain.model.Account}
 * is not found in data store
 *
 * */
public class NonexistentAccountException extends Exception {

    public NonexistentAccountException(String message) {
        super(message);
    }
}
