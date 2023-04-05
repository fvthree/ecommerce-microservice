package com.fvthree.CategoryService.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fvthree.CategoryService.dto.CreateCategoryDTO;
import com.fvthree.CategoryService.entity.Category;
import com.fvthree.CategoryService.repository.CategoryRepository;
import com.fvthree.CategoryService.service.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
	
	@MockBean
	private CategoryRepository categoryRepository;
	
	@MockBean
	private CategoryService categoryService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper = new ObjectMapper()
			.findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@DisplayName("Create Category - is Success")
	@Test
	public void createCategoryTest() throws Exception {
		var createCategory = getCreateCategoryDTO();
		var category = getMockCategory();
		var jsonPost = objectMapper.writeValueAsString(createCategory);
		
		when(categoryRepository.save(any(Category.class))).thenReturn(category);
		when(categoryRepository.existsByName(any())).thenReturn(false);
		when(categoryService.create(any(CreateCategoryDTO.class))).thenReturn(category);

		this.mockMvc.perform(post("/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPost))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("New Category has been saved with ID:1"));
	}
	
	@DisplayName("Get Category by slug - is Success")
	@Test
	public void get_Category_by_slug_is_success() throws Exception {
		var category = getMockCategory();
		
		when(categoryRepository.findBySlug(any())).thenReturn(Optional.ofNullable(category));
		when(categoryService.read(any())).thenReturn(category);
		
		this.mockMvc.perform(get("/category/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(category.getId()))
			.andExpect(jsonPath("$.name").value(category.getName()))
			.andExpect(jsonPath("$.slug").value(category.getSlug()));
	}
	
	private CreateCategoryDTO getCreateCategoryDTO() {
		return CreateCategoryDTO.builder().name("books").build();
	}
	
	private Category getMockCategory() {
		return Category.builder()
				.id(1L)
				.name("books")
				.slug("books")
				.build();
	}
}
