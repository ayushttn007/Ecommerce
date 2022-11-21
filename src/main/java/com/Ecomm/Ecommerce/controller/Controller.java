package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.dto.*;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.UserAccountService;
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

@Autowired
    UserAccountService userAccountService;


    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> home(){
        return ResponseEntity.ok().body("Welcome");
    }
    // Get mapping for products
    @GetMapping(path = "api/products")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> products(){
        return ResponseEntity.ok().body("List of Products");
    }

    // Register Api for Seller
    @PostMapping(path = "api/register",headers = "Role=CUSTOMER")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register( @Valid @RequestBody CustomerDto customerDto,HttpServletRequest request){
        String siteUrl = emailService.getSiteURL(request);
        userService.registerCustomer(customerDto,"CUSTOMER",siteUrl);
        return ResponseEntity.ok().body("Please check your email to verify your account.");
    }

    // Register APi for Seller
    @PostMapping(path = "api/register",headers = "Role=SELLER")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody SellerDto sellerDto ){
        userService.registerSeller(sellerDto,"SELLER");
      return ResponseEntity.ok().body("Register Successfully");
    }

    // verify api for register account verification and  send verification link again if expire.
    @GetMapping(path ="/confirm")
    @PostMapping(path ="/confirm")
    // @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> verifyUserEmail(@Param("token") String token, HttpServletRequest request){
        String siteUrl = emailService.getSiteURL(request);
        String responseMessage = emailService.verifyVerificationToken(token,siteUrl);
       return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
    }

    // api to re-send activation link using email.
    @PostMapping(path ="/api/resend_token")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> resendVerification(@Valid @RequestBody EmailDto emailDto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String siteUrl = emailService.getSiteURL(request);
        String userEmail = emailDto.getEmail();
       String responseMessage = emailService.regenerateToken(userEmail, siteUrl);
        return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
    }
    // api for logout user
    @PostMapping(path = "api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
       return userAccountService.userLogout(request);
    }

    // api for login user

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String userEmail = loginDto.getEmail();
        String userPassword = loginDto.getPassword();
       String responseMessage =  userAccountService.userLogin(userEmail, userPassword);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(path="/forgot_password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody EmailDto emailDtO){
        String userEmail = emailDtO.getEmail();
        String response = userAccountService.userForgotPassword(userEmail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path= "/reset_password")
    public ResponseEntity<String> resetPassword(@Param("token") String token, @Valid @RequestBody PasswordDto passwordDto, HttpServletRequest request){
        String siteUrl = emailService.getSiteURL(request);
        String userPassword = passwordDto.getPassword();
        String userConfirmPassword = passwordDto.getConfirmPassword();
        String responseMessage = emailService.resetPasswordEmail(token,userPassword,userConfirmPassword);
        return new ResponseEntity<>(responseMessage,HttpStatus.ACCEPTED);
    }

}
