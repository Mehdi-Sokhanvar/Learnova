package org.learnova.lms.service.course;

import jakarta.transaction.Transactional;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.user.Student;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.dto.response.CourseResponseDTO;
import org.learnova.lms.dto.response.StudentResponse;
import org.learnova.lms.dto.request.EnrollmentRoleForUser;

import org.learnova.lms.exception.*;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.user.StudentRepository;
import org.learnova.lms.repository.user.TeacherRepository;
import org.learnova.lms.repository.user.UserRepository;

import org.learnova.lms.util.Messages;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Optional<Course> addCourse(CourseRequestDTO course) {
        LocalDate startDate = LocalDate.parse(course.startDate());
        LocalDate endDate = LocalDate.parse(course.endDate());

        if (startDate.isAfter(endDate)) {
            throw new DateAndTimeException(Messages.START_DATE_CANNOT_BE_BEFORE_END_DATE);
        }
        Course courses = new Course(
                course.name(),
                course.description(),
                LocalDate.parse(course.startDate()),
                LocalDate.parse(course.endDate()),
                UUID.randomUUID()
        );
        return Optional.of(courseRepository.save(courses));
    }

    @Transactional
    @Override
    public void assignUserRoleInCourses(EnrollmentRoleForUser user) {

        AppUser userFound = userRepository.findById(user.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(Messages.USER_NOT_FOUND_BY_THIS_ID, user.userId())));

        Course courseFound = courseRepository.findById(user.courseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, user.courseId())));

        switch (userFound.getRole().getName()) {
            case "TEACHER" -> {
                Teacher teacher=(Teacher) userFound;
                courseFound.setTeacher(teacher);
            }
            case "STUDENT" -> {
                Student student = (Student) userFound;
                if (!courseFound.getStudentList().contains(student)) {
                    courseFound.getStudentList().add(student);
                    student.getCourseList().add(courseFound);
                }
            }
            default -> throw new IllegalArgumentException("Unsupported role");
        }

        courseRepository.save(courseFound);
        userRepository.save(userFound);

        //todo : writing better this code
    }

    @Override
    public void updateCourse(Long id, CourseRequestDTO course) {
        Course courseFound = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, id)));

        courseRepository.findById(id).ifPresentOrElse(
                existingCourse -> {
                    existingCourse.setTitle(course.name());
                    existingCourse.setDescription(course.description());
                    existingCourse.setStartDate(LocalDate.parse(course.startDate()));
                    existingCourse.setEndDate(LocalDate.parse(course.endDate()));

                    courseRepository.save(existingCourse);
                }, () -> {
                    throw new CourseNotFoundException(String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, id));
                });

    }

    @Override
    public void deleteCourse(Long courseId) {
        Course courseFound = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, courseId)));
        courseRepository.delete(courseFound);
    }

    @Override
    public void deleteUserFromCourse(Long userId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, courseId)
                ));
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(Messages.USER_NOT_FOUND_BY_THIS_ID, userId)
                ));
        if (!course.getStudentList().contains(student)) {
            throw new UserNotEnrolledException(
                    String.format(Messages.USER_NOT_ENROLLED_IN_COURSE, userId, courseId));
        }

        course.getStudentList().remove(student);
        student.getCourseList().remove(course);

        courseRepository.save(course);
        studentRepository.save(student);
    }

    @Override
    public List<StudentResponse> listStudentsForCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, courseId)));
        return course.getStudentList().stream()
                .map(student -> new StudentResponse(
                        student.getUserName(),
                        student.getEmail()
                )).collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDTO> listOfUserCourses(AppUser userLogin) {
        List<Course> courses = null;
        if (userLogin.getRole().getName().equals("TEACHER")) {
//            courses = courseRepository.findCourseByTeacherId(userLogin.getId());
            courses = teacherRepository.findById(userLogin.getId()).get().getCourseList();
            if (courses.isEmpty()) {
                throw new TeacherNotAssignedThisCourse(String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, userLogin));
            }
        } else if (userLogin.getRole().getName().equals("STUDENT")) {
            courses = studentRepository.findById(userLogin.getId()).get().getCourseList();
            if (courses.isEmpty()) {
                throw new StudnetNotAssignedThisCourse(String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, userLogin));
            }
        }
        return courses
                .stream()
                .map(course -> new CourseResponseDTO(
                        course.getTitle(),
                        course.getStartDate(),
                        course.getEndDate()
                )).collect(Collectors.toList());
    }
}
