package com.Ecomm.Ecommerce.Dto;


import com.Ecomm.Ecommerce.entities.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
public class CategoryDto {

    private long id;

    private String name;

    private Long parentId;

}
