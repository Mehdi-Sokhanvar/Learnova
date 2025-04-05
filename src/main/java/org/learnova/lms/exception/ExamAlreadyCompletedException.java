package org.learnova.lms.exception;

public class ExamAlreadyCompletedException extends RuntimeException {
    public ExamAlreadyCompletedException(String message) {
        super(message);
    }
}
