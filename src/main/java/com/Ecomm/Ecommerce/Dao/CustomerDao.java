package com.Ecomm.Ecommerce.Dao;

import com.Ecomm.Ecommerce.dto.AddressDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter

public class CustomerDao {
    private long id;
    private String fullName;
    private String email;
    private boolean is_active;



}
