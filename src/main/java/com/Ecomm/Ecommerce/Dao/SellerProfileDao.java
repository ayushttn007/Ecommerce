package com.Ecomm.Ecommerce.Dao;

import com.Ecomm.Ecommerce.entities.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SellerProfileDao {
    private long userid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean is_active;
    private  String companyContact;
    private  String companyName;
    private String gst;
    private Address address;
}
