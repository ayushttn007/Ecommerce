package com.Ecomm.Ecommerce.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MetaFieldValueDto {
    Long categoryId;
    Long metadataId;
    List<String> values;
}