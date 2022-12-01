package com.Ecomm.Ecommerce.Dto.ResponseDto;

import com.Ecomm.Ecommerce.entities.Address;
import lombok.Data;

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
    private byte[] image;
    private Address address;
}
