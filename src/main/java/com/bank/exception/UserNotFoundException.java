package com.bank.exception;

import java.util.UUID;

public class UserNotFoundException extends BankingException {
    public UserNotFoundException(String username) {
        super("User with username: " + username + " does not exist.");
    }
    public UserNotFoundException(UUID userId) {
        super("User with userid: " + userId + " does not exist.");
    }
}
