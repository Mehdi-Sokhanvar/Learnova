package org.learnova.lms.exception;

public class ExamNotStartedException extends RuntimeException {
    public ExamNotStartedException(String message) {
        super(message);
    }
}
