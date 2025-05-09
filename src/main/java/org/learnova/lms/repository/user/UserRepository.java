package org.learnova.lms.repository.user;

import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findUserByEmail( String email);

    boolean existsByEmail( String email);

    List<AppUser> findAppUserByStatus(Status userStatus);

}

//todo » کاربرد این انوتیسشن ها جی