package com.fvthree.CategoryService.service;

import org.springframework.stereotype.Service;

import com.fvthree.CategoryService.dto.CategoriesPagedResponse;
import com.fvthree.CategoryService.dto.CreateCategoryDTO;
import com.fvthree.CategoryService.entity.Category;

@Service
public interface CategoryService {
	Category create(CreateCategoryDTO categoryDTO);
	Category update(Long id, CreateCategoryDTO categoryDTO);
	void remove(Long id);
	Category read(String slug);
	CategoriesPagedResponse list(int pageNo, int pageSize, String sortBy, String sortDir);
}
