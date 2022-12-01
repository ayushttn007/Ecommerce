package com.Ecomm.Ecommerce.Dto.ResponseDto;

import com.Ecomm.Ecommerce.Dto.ChildCategoryDto;
import com.Ecomm.Ecommerce.entities.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CategoryResponseDto {
    private long id;
    private String name;
    private Category parent;
    private Set<ChildCategoryDto> children = new HashSet<>();
    private List<MetadataResponseDTO> metadataList = new ArrayList<>();

}