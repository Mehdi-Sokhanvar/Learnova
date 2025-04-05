package org.learnova.lms.exception;

public class QuestionAlreadyExistInExam extends RuntimeException {
    public QuestionAlreadyExistInExam(String message) {
        super(message);
    }
}
