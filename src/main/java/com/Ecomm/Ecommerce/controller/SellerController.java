package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.DTO.PasswordDto;
import com.Ecomm.Ecommerce.DTO.ResponseDTO.SellerProfileDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.AddressUpdateDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.SellerUpdateDto;
import com.Ecomm.Ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SellerController {


    @Autowired
    SellerService sellerService;

    @GetMapping("/seller/profile")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<SellerProfileDto> sellerProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        System.out.println(authentication);
        SellerProfileDto seller = sellerService.getSellerProfile(userEmail);
        return new ResponseEntity<>(seller, HttpStatus.ACCEPTED);
    }


    @PatchMapping("/seller/update-profile")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> UpdateProfile(Authentication authentication,@Valid  @RequestBody SellerUpdateDto sellerUpdateDao){
        String userEmail = authentication.getName();
         String responseMessage = sellerService.updateSellerProfile(userEmail,sellerUpdateDao);
         return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/seller/update-password")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> UpdatePassword(Authentication authentication,@Valid @RequestBody PasswordDto sellerPasswordDao){
        String userEmail = authentication.getName();
        String responseMessage = sellerService.updateSellerPassword(userEmail,sellerPasswordDao);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/seller/update_address")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> UpdateAddress(Authentication authentication, @RequestParam long address_id,  @Valid @RequestBody AddressUpdateDto addressDao){
        String userEmail = authentication.getName();
        String responseMessage = sellerService.updateSellerAddress(userEmail, addressDao,address_id);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
}
