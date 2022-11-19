package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.impl.UserService;
import com.Ecomm.Ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
public class Controller {

@Autowired  UserService userService;

@Autowired  UserRepo userRepo;

@Autowired  EmailService emailService;


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
    public ResponseEntity<String> register( @Valid @RequestBody CustomerDto customerDto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        userService.registerCustomer(customerDto,"CUSTOMER",emailService.getSiteURL(request));
        return ResponseEntity.ok().body("Please check your email to verify your account.");
    }

    // Register APi for Seller
    @PostMapping(value = "api/register",headers = "Role=SELLER")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody SellerDto sellerDto, HttpServletRequest request ) throws MessagingException, UnsupportedEncodingException {

        userService.registerSeller(sellerDto,"SELLER",emailService.getSiteURL(request));
      return ResponseEntity.ok().body("Register Successfully");
    }

    // verify api for register account verification
    @GetMapping("/confirm")
//    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> verifyUserEmail(@Param("code") String code) {

        if (emailService.verify(code)) {
            return ResponseEntity.ok().body("Congratulations, your account has been verified.");
        } else {
            return ResponseEntity.badRequest().body("Sorry,We could not verify account,It could maybe already verified,or verification link has been Expired");
        }
    }


    // api for logout user
    @PostMapping("api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
       return userService.userLogout(request);
    }

    // api for login user



}
