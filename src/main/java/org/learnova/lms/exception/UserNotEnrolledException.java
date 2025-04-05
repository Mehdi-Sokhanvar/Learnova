package org.learnova.lms.exception;

public class UserNotEnrolledException extends RuntimeException {
    public UserNotEnrolledException(String message) {
        super(message);
    }
}
