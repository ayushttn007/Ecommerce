package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.Dao.PasswordDao;
import com.Ecomm.Ecommerce.Dao.SellerAddressDao;
import com.Ecomm.Ecommerce.Dao.SellerDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.service.SellerService;
import com.Ecomm.Ecommerce.service.impl.SellerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class SellerController {


    @Autowired
    SellerService sellerService;

    @GetMapping("/seller/profile")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<SellerProfileDao> sellerProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        System.out.println(authentication);
        SellerProfileDao seller = sellerService.getSellerProfile(userEmail);
        return new ResponseEntity<>(seller, HttpStatus.ACCEPTED);
    }


    @PatchMapping("/seller/update-profile")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> UpdateProfile(Authentication authentication,@RequestBody SellerProfileDao sellerProfileDao){
        String userEmail = authentication.getName();
         String responseMessage = sellerService.updateSellerProfile(userEmail,sellerProfileDao);
         return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/seller/update-password")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> UpdatePassword(Authentication authentication,@RequestBody PasswordDao sellerPasswordDao){
        String userEmail = authentication.getName();
        String responseMessage = sellerService.updateSellerPassword(userEmail,sellerPasswordDao);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/seller/update_address")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> UpdateAddress(Authentication authentication, @RequestParam long address_id, @RequestBody SellerAddressDao sellerAddressDao){
        String userEmail = authentication.getName();
        String responseMessage = sellerService.updateSellerAddress(userEmail,sellerAddressDao,address_id);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
}
