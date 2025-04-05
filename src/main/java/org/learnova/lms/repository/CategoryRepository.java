package org.learnova.lms.repository;

import org.learnova.lms.domain.question.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
