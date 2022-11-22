package com.Ecomm.Ecommerce.Dao;

import com.Ecomm.Ecommerce.entities.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerProfileDao {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean is_active;
    private  String companyContact;
    private  String companyName;
    private Address address;
}
