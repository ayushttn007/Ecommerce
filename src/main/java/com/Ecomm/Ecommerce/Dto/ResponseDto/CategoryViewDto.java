package com.Ecomm.Ecommerce.Dto.ResponseDto;

import com.Ecomm.Ecommerce.Dto.ChildCategoryDto;
import com.Ecomm.Ecommerce.entities.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CategoryViewDto {
    private long id;
    private String name;
    private Category parent;
    private Set<ChildCategoryDto> children = new HashSet<>();
}