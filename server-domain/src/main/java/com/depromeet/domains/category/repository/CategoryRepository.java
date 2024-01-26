package com.depromeet.domains.category.repository;

import java.util.Optional;

import com.depromeet.domains.category.entity.Category;
import com.depromeet.enums.CategoryType;

import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryType(CategoryType categoryType);
}
