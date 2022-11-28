package com.Ecomm.Ecommerce.DTO.ResponseDTO;

import com.Ecomm.Ecommerce.entities.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
// Details shown to seller - View my profile
public class SellerProfileDto {
    private long userid;
    private String firstName;
    private String lastName;
    private boolean is_active;
    private  String companyContact;
    private  String companyName;
    private String gst;
    private Address address;
}
