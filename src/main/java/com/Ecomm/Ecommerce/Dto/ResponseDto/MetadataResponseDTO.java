package com.Ecomm.Ecommerce.Dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetadataResponseDTO {
    private Long metadataId;
    private String fieldName;
    private String possibleValues;
}