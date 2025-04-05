package org.learnova.lms.exception;

public class ScoreCanNotBiggerThanScoreException extends RuntimeException {
    public ScoreCanNotBiggerThanScoreException(String message) {
        super(message);
    }
}
