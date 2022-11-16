package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.dto.UserDto;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.repos.UserRepo;
import com.Ecomm.Ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

@Autowired  UserService userService;

@Autowired  UserRepo userRepo;


// Register Api for Seller
    @PostMapping(value = "/register",headers = "Role=CUSTOMER")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody CustomerDto customerDto){

        userService.registerCustomer(customerDto,"CUSTOMER");
    }

// Register APi for Seller
    @PostMapping(value = "/register",headers = "Role=SELLER")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody SellerDto sellerDto){

        userService.registerSeller(sellerDto,"SELLER");
    }
}
