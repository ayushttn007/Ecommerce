package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.dto.UserDto;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.repos.UserRepo;
import com.Ecomm.Ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
public class Controller {

@Autowired  UserService userService;

@Autowired  UserRepo userRepo;

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> home(){
        return ResponseEntity.ok().body("Welcome");
    }
   // Get mapping for products
    @GetMapping(value = "api/products")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> products(){
        return ResponseEntity.ok().body("List of Products");
    }

// Register Api for Seller
    @PostMapping(value = "api/register",headers = "Role=CUSTOMER")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@RequestBody CustomerDto customerDto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        userService.registerCustomer(customerDto,"CUSTOMER",getSiteURL(request));
        return ResponseEntity.ok().body("Please check your email to verify your account.");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    // Register APi for Seller
    @PostMapping(value = "api/register",headers = "Role=SELLER")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@RequestBody SellerDto sellerDto ){

        userService.registerSeller(sellerDto,"SELLER");
      return ResponseEntity.ok().body("Register Successfully");
    }

//    @PostMapping(value = "api/login", headers = "Role=CUSTOMER")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<String> login(@RequestBody )


    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "Congratulations, your account has been verified.";
        } else {
            return "Sorry,We could not verify account,It could maybe already verified,or verification link has been Expired";
        }
    }

}
