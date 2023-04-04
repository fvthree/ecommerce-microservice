package com.fvthree.CategoryService.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fvthree.CategoryService.dto.CategoriesPagedResponse;
import com.fvthree.CategoryService.dto.CreateCategoryDTO;
import com.fvthree.CategoryService.entity.Category;
import com.fvthree.CategoryService.exceptions.HTTP400Exception;
import com.fvthree.CategoryService.exceptions.HTTP404Exception;
import com.fvthree.CategoryService.exceptions.RestAPIExceptionInfo;
import com.fvthree.CategoryService.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HTTP400Exception.class)
	public @ResponseBody RestAPIExceptionInfo handleBadRequestException(HTTP400Exception ex,
			WebRequest request, HttpServletResponse response) {
		log.info("Received Bad Request Exception", ex);
		return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The request did not have correct parameters.");
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(HTTP404Exception.class)
	public @ResponseBody RestAPIExceptionInfo handleResourceNotFoundException(HTTP404Exception ex,
			WebRequest request, HttpServletResponse response) {
		log.info("Received Resource Not Found Exception", ex);
		return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The requested resource was not found.");
	}
	
	@Autowired
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CreateCategoryDTO categoryDTO) {
		Category category = categoryService.create(categoryDTO);
		return ResponseEntity.ok().body("New Category has been saved with ID:" + category.getId());
	}
	
	@GetMapping("/all")
	public CategoriesPagedResponse getAllCategories(
			@RequestParam(value="pageNo",defaultValue="0",required=false) int pageNo,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="sortBy",defaultValue="dateCreated",required=false) String sortBy,
			@RequestParam(value="sortDir",defaultValue="desc",required=false) String sortDir) {
		return categoryService.list(pageNo, pageSize, sortBy, sortDir);
	}
	
	@GetMapping("/{slug}")
	public ResponseEntity<?> getCategoryBySlug(@PathVariable(name="slug") String slug) {
		return ResponseEntity.ok(categoryService.read(slug));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody CreateCategoryDTO categoryDto, @PathVariable(name="id") Long id) {
		return new ResponseEntity<>(categoryService.update(id, categoryDto), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(name="id") Long id) {
		categoryService.remove(id);
		return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
	}
	
}
