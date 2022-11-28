package com.Ecomm.Ecommerce.DTO;


import lombok.Getter;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Seller} entity
 */
@Getter
@Setter
public class SellerDto extends UserDto {

    @NotNull(message = "GST number is mandatory field.")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = "Enter a valid GST number")
    private  String gst;

    @NotNull(message = "Company Number is mandatory field.")
    @Size(min=10,max=10,message = "Enter a valid Contact number.")
    private  String companyContact;

    @NotEmpty(message = "Company Name is mandatory.")
    @Size(min = 2, max=30, message = "Enter a valid company name.")
    private  String companyName;

    @Valid
    private  AddressDto address;
}