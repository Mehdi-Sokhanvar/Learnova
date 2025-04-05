package org.learnova.lms.exception;

public class TeacherNotAssignedThisCourse extends RuntimeException {
    public TeacherNotAssignedThisCourse(String message) {
        super(message);
    }
}
