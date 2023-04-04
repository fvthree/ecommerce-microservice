package com.fvthree.CategoryService.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCategoryDTO {
	@NotEmpty
	private String name;
}
