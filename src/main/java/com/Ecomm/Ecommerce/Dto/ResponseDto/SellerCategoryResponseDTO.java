package com.Ecomm.Ecommerce.Dto.ResponseDto;

import com.Ecomm.Ecommerce.entities.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SellerCategoryResponseDTO {
   private Long id;
   private String name;
   private Category parent;
   private List<MetadataResponseDTO> metadata;
}