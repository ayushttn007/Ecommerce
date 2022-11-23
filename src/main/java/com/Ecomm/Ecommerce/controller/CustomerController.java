package com.Ecomm.Ecommerce.controller;

import com.Ecomm.Ecommerce.Dao.CustomerProfileDao;
import com.Ecomm.Ecommerce.Dao.PasswordDao;
import com.Ecomm.Ecommerce.Dao.SellerAddressDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.dto.AddressDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

  @Autowired
    CustomerService customerService;

    @GetMapping("/customer/profile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CustomerProfileDao> customerProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        CustomerProfileDao customer = customerService.getCustomerProfile(userEmail);
        return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);
    }

    @GetMapping("/customer/address")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List> UpdateAddress(Authentication authentication, @RequestBody SellerAddressDao customerAddressDao){
        String userEmail = authentication.getName();
        List<Address> customerAddress = customerService.getCustomerAddress(userEmail,customerAddressDao);
        return new ResponseEntity<>(customerAddress,HttpStatus.OK);
    }

    @PatchMapping("/customer/update_profile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> UpdateProfile(Authentication authentication,@RequestBody CustomerProfileDao customerProfileDao){
        String userEmail = authentication.getName();
        String responseMessage = customerService.updateProfile(userEmail,customerProfileDao);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/customer/update_password")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> UpdatePassword(Authentication authentication,@RequestBody PasswordDao sellerPasswordDao){
        String userEmail = authentication.getName();
        String responseMessage = customerService.updatePassword(userEmail,sellerPasswordDao);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
}
