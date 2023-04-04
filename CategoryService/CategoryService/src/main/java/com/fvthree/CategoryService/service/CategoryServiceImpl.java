package com.fvthree.CategoryService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.CategoryService.dto.CategoriesPagedResponse;
import com.fvthree.CategoryService.dto.CreateCategoryDTO;
import com.fvthree.CategoryService.entity.Category;
import com.fvthree.CategoryService.exceptions.HTTP400Exception;
import com.fvthree.CategoryService.exceptions.HTTP404Exception;
import com.fvthree.CategoryService.repository.CategoryRepository;
import com.github.slugify.Slugify;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category create(CreateCategoryDTO categoryDTO) {
		var slugifiedName = new Slugify().slugify(categoryDTO.getName());
		
		if (categoryRepository.existsByName(categoryDTO.getName())) {
			throw new HTTP400Exception("Category already exists.");
		}
		
		var newCategory = Category.builder()
				.name(categoryDTO.getName())
				.slug(slugifiedName)
				.build();
		
		return categoryRepository.save(newCategory);
	}

	@Override
	public Category update(Long id, CreateCategoryDTO categoryDTO) {
		String slugifiedName = new Slugify().slugify(categoryDTO.getName());
		
		Category dbCat = categoryRepository.findById(id)
				.orElseThrow(() -> new HTTP404Exception("Category doesnt exists."));
		
		if (!categoryDTO.getName().equalsIgnoreCase(dbCat.getName())) {
			if (categoryRepository.existsByName(categoryDTO.getName())) {
				throw new HTTP400Exception("Category already exists.");
			}
		}
		
		dbCat.setName(categoryDTO.getName());
		dbCat.setSlug(slugifiedName);
		
		return categoryRepository.save(dbCat);
	}

	@Override
	public void remove(Long id) {
		categoryRepository.findById(id)
			.orElseThrow(() -> new HTTP404Exception("Category not found with id: " + id));
		categoryRepository.deleteById(id);
	}

	@Override
	public Category read(String slug) {
		return categoryRepository.findBySlug(slug)
				.orElseThrow(()-> new HTTP404Exception("Category not found."));
	}

	@Override
	public CategoriesPagedResponse list(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() 
				: Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Category> categories = categoryRepository.findAll(pageable);
		
		List<Category> content = categories.getContent();
		
		CategoriesPagedResponse categoryResponse = CategoriesPagedResponse.builder()
				.content(content)
				.pageNo(categories.getNumber())
				.pageSize(categories.getSize())
				.totalElements(categories.getTotalElements())
				.totalPages(categories.getTotalPages())
				.last(categories.isLast())
				.build();
		
		return categoryResponse;
	}

}
