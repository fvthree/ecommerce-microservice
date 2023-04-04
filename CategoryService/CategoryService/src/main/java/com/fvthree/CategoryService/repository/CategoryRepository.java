package com.fvthree.CategoryService.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.CategoryService.entity.Category;

@Transactional
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
	Optional<Category> findBySlug(String slug);
	boolean existsByName(String name);
	boolean existsBySlug(String slug);
}
