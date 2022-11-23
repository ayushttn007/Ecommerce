package com.Ecomm.Ecommerce.Dao;

import com.Ecomm.Ecommerce.entities.Address;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SellerProfileDao {
    private long userid;
    @Size(min = 2, max = 30, message = "Must contain 2-15 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "First Name can only contain alphabets.")
    private String firstName;
    private String lastName;
    private String email;
    private boolean is_active;
    private  String companyContact;
    private  String companyName;
    private String gst;
    private Address address;
}
