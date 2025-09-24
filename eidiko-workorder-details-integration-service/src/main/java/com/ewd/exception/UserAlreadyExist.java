package com.ewd.exception;

public class UserAlreadyExist extends RuntimeException {
    public UserAlreadyExist() {
    }

    public UserAlreadyExist(String message) {
        super(message);
    }
}
