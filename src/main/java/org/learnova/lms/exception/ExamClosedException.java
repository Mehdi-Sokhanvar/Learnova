package org.learnova.lms.exception;

public class ExamClosedException extends RuntimeException {
    public ExamClosedException(String message) {
        super(message);
    }
}
