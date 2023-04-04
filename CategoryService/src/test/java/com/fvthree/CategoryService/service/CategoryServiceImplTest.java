package com.fvthree.CategoryService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.fvthree.CategoryService.dto.CreateCategoryDTO;
import com.fvthree.CategoryService.entity.Category;
import com.fvthree.CategoryService.exceptions.HTTP400Exception;
import com.fvthree.CategoryService.repository.CategoryRepository;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceImplTest {
	
	@Mock 
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	CategoryService categoryService = new CategoryServiceImpl();
	
	@DisplayName("Create Category - Success Scenario")
	@Test
	void test_when_Create_Category_Success() {
		// Mock
		var category = getMockedCategory();
		var createCategory = getCreateCategory();
		// When
		when(categoryRepository.existsByName(any())).thenReturn(false);
		when(categoryRepository.save(any(Category.class))).thenReturn(category);
		// Actual
		var savedCategory = categoryService.create(createCategory);
		// Then
		assertNotNull(savedCategory);
		assertEquals(savedCategory.getId(), category.getId());
		assertEquals(savedCategory.getName(), category.getName());
		assertEquals(savedCategory.getSlug(), category.getSlug());
		assertEquals(savedCategory.getDateCreated(), category.getDateCreated());
	}
	
	@DisplayName("Create Categoy - Name Already Exist")
	@Test
	void test_when_Create_Category_Name_Exists() {
		// Mock
		var createCategory = getCreateCategory();
		// When
		when(categoryRepository.existsByName(any())).thenReturn(true);
		// Actual
		HTTP400Exception exception = assertThrows(HTTP400Exception.class,
				() -> categoryService.create(createCategory));
		// Then
		assertEquals("Category already exists.", exception.getMessage());
	}
	
	@DisplayName("Get Category - Success By Slug")
	@Test
	void test_when_Get_Category() {
		// Mock
		var category = getMockedCategory();
		// When
		when(categoryRepository.findBySlug(any())).thenReturn(Optional.of(category));
		//Actual
		var retrievedCategory = categoryService.read(category.getSlug());
		//Then
		assertNotNull(retrievedCategory);
		assertEquals(retrievedCategory.getName(), category.getName());
		assertEquals(retrievedCategory.getSlug(), category.getSlug());
	}
	
	@DisplayName("Update Category - Success")
	@Test
	void test_when_Update_Category() {
		// Mock
		var category = getMockedCategory();
		var updateCategory = getCreateCategory();
		// When
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(categoryRepository.existsByName(any())).thenReturn(false);
		when(categoryRepository.save(any(Category.class))).thenReturn(category);
		// Actual
		var updatedCategory = categoryService.update(1L, updateCategory);
		// Then
		assertNotNull(updatedCategory);
		assertEquals(updatedCategory.getName(), category.getName());
		assertEquals(updatedCategory.getSlug(), category.getSlug());
		assertEquals(updatedCategory.getLastUpdated(), category.getLastUpdated());	
	}
	
	@DisplayName("List Category - Success (2)")
	@Test
	void test_when_List_Category() {
		// Mock
		var category = getMockedCategory();
		var secondcategory = get2ndMockedCategory();
		var categories = new ArrayList<Category>();
		categories.add(category);
		categories.add(secondcategory);
		Page categoriesPage = new PageImpl<Category>(categories);
		
		// When
		when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoriesPage);
		
		// Actual
		var list = categoryService.list(0, 10, "createdBy", "desc");
		
		// Then
		assertNotNull(list);
		assertEquals(list.getPageSize(), 2);
	}
	
	private CreateCategoryDTO getCreateCategory() {
		return CreateCategoryDTO.builder()
				.name("books")
				.build();
	}
	
	private Category getMockedCategory() {
		return Category.builder()
				.id(1L)
				.name("books")
				.slug("books")
				.dateCreated(LocalDateTime.now())
				.lastUpdated(LocalDateTime.now())
				.build();
	}
	
	private Category get2ndMockedCategory() {
		return Category.builder()
				.id(1L)
				.name("bags")
				.slug("bags")
				.dateCreated(LocalDateTime.now())
				.lastUpdated(LocalDateTime.now())
				.build();
	}

}
