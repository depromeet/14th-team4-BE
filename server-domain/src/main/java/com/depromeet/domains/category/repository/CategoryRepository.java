package com.depromeet.domains.category.repository;

import com.depromeet.domains.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
