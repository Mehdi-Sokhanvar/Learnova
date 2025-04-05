package org.learnova.lms.service.course;

import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.dto.response.CourseResponseDTO;
import org.learnova.lms.dto.response.StudentResponse;
import org.learnova.lms.dto.request.EnrollmentRoleForUser;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Optional<Course> addCourse(CourseRequestDTO course);

    void assignUserRoleInCourses(EnrollmentRoleForUser user);


    void updateCourse(Long id,CourseRequestDTO course);

    void deleteCourse(Long courseId);

    void deleteUserFromCourse(Long userId, Long courseId);

    List<StudentResponse> listStudentsForCourse(Long courseId);

    List<CourseResponseDTO> listOfUserCourses(AppUser userLogin);
}
