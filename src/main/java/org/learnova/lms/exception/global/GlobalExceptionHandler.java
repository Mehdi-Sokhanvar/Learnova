package org.learnova.lms.exception.global;

import org.learnova.lms.exception.*;
import org.learnova.lms.exception.ExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(RuntimeException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> roleNotFoundExceptions(RoleNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserExistInDataBase.class)
    public ResponseEntity<org.learnova.lms.exception.ExceptionResponse> userExistInDataBaseExceptions(UserExistInDataBase e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return new ResponseEntity<>(new ExceptionResponse(errors.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFoundExceptions(UserNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateAndTimeException.class)
    public ResponseEntity<ExceptionResponse> dateAndTimeException(DateAndTimeException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ExceptionResponse> courseNotFoundException(CourseNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<ExceptionResponse> examNotFoundException(ExamNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TeacherNotAssignedThisCourse.class)
    public ResponseEntity<ExceptionResponse> teacherNotAssignedThisCourse(TeacherNotAssignedThisCourse e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataExamBetweenStartDateAndEndDateException.class)
    public ResponseEntity<ExceptionResponse> dataExamBetweenStartDateAndEndDateException(DataExamBetweenStartDateAndEndDateException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ExamNotStartedException.class)
    public ResponseEntity<ExceptionResponse> IllegalStateExceptions(ExamNotStartedException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ExamAlreadyCompletedException.class)
    public ResponseEntity<ExceptionResponse> examAlreadyCompletedException(ExamAlreadyCompletedException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExamExpiredException.class)
    public ResponseEntity<ExceptionResponse> examExpiredException(ExamExpiredException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> sessionNotFoundException(SessionNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> unauthorizedAccessException(UnauthorizedAccessException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidNavigationException.class)
    public ResponseEntity<ExceptionResponse> invalidNavigationException(InvalidNavigationException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
