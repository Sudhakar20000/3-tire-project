package com.ewd.exception;

public class MissingFilterException extends RuntimeException {
    public MissingFilterException(String message) {
        super(message);
    }
}
