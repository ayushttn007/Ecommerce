package com.Ecomm.Ecommerce.Dao;

import com.Ecomm.Ecommerce.entities.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CustomerProfileDao {
    private long userid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean is_active;
    private  String contact;

    private List<Address> addresses;

}
