package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.dto.UserDto;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.impl.EmailServiceImpl;
import com.Ecomm.Ecommerce.service.impl.UserServiceImpl;
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

@Autowired
UserServiceImpl userService;

@Autowired  UserRepo userRepo;

@Autowired
EmailServiceImpl emailService;


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
    public ResponseEntity<String> register( @Valid @RequestBody CustomerDto customerDto,HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String siteUrl = emailService.getSiteURL(request);
        userService.registerCustomer(customerDto,"CUSTOMER",siteUrl);
        return ResponseEntity.ok().body("Please check your email to verify your account.");
    }

    // Register APi for Seller
    @PostMapping(value = "api/register",headers = "Role=SELLER")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody SellerDto sellerDto, HttpServletRequest request ) throws MessagingException, UnsupportedEncodingException {
        String siteUrl = emailService.getSiteURL(request);
        userService.registerSeller(sellerDto,"SELLER",siteUrl);
      return ResponseEntity.ok().body("Register Successfully");
    }

    // verify api for register account verification and  send verification link again.
    @GetMapping(value ="/confirm")
    @PostMapping(value ="/confirm")
//    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> verifyUserEmail(@Param("token") String token, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String siteUrl = emailService.getSiteURL(request);
        String responseMessage = emailService.verifyVerificationToken(token,siteUrl);
       return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
    }

// api to re-send activation link using email
    @PostMapping(value="/api/resend_token")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> resendVerification(@RequestBody UserDto user,HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
       String userEmail = user.getEmail();
        String siteUrl = emailService.getSiteURL(request);
       String responseMessage = emailService.regenerateToken(userEmail,siteUrl);
        return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
    }
    // api for logout user
    @PostMapping("api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
       return userService.userLogout(request);
    }

    // api for login user



}
