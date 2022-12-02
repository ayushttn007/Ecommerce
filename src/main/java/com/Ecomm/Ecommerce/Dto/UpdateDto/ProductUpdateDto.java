package com.Ecomm.Ecommerce.Dto.UpdateDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductUpdateDto {
    private String description;
    private Boolean isCancellable;
    private Boolean isReturnable;
    private String name;

}