package org.learnova.lms.exception;

public class UserExistInDataBase extends RuntimeException {
    public UserExistInDataBase(String message) {
        super(message);
    }
}
