package com.Ecomm.Ecommerce.Dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaFieldValueResponseDto {
    private Long categoryId;
    private Long metaFieldId;
    private String values;
}