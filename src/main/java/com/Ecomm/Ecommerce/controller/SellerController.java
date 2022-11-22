package com.Ecomm.Ecommerce.controller;


import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.service.impl.SellerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController {


    @Autowired
    SellerServiceImpl sellerService;

    @GetMapping("/seller/profile")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<SellerProfileDao> sellerProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        System.out.println(authentication);
        SellerProfileDao seller = sellerService.getSellerProfile(userEmail);
        return new ResponseEntity<>(seller, HttpStatus.ACCEPTED);
    }


}
