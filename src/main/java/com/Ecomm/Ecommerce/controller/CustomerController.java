package com.Ecomm.Ecommerce.controller;

import com.Ecomm.Ecommerce.Dto.AddressDto;
import com.Ecomm.Ecommerce.Dto.PasswordDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.AddressUpdateDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CustomerProfileDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CustomerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CustomerController {

  @Autowired
    CustomerService customerService;

    @GetMapping("/customer/profile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CustomerProfileDto> customerProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        CustomerProfileDto customer = customerService.getCustomerProfile(userEmail);
        return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);
    }

    @GetMapping("/customer/address")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List> getAddress(Authentication authentication){
        String userEmail = authentication.getName();
        List<Address> customerAddress = customerService.getCustomerAddress(userEmail);
        return new ResponseEntity<>(customerAddress,HttpStatus.OK);
    }

    @PatchMapping("/customer/update/profile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> UpdateProfile(Authentication authentication,
                                                @RequestPart("image") MultipartFile image,
                                                @Valid @RequestPart("details") CustomerUpdateDto customerUpdateDto){
        String userEmail = authentication.getName();
        String responseMessage = customerService.updateProfile(userEmail,customerUpdateDto , image);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/customer/update/password")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> UpdatePassword(Authentication authentication,@Valid @RequestBody PasswordDto customerPasswordDto){
        String userEmail = authentication.getName();
        String responseMessage = customerService.updatePassword(userEmail,customerPasswordDto);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PatchMapping("/customer/update/address")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public  ResponseEntity<String> updateAddress(Authentication authentication, @Valid @RequestBody AddressUpdateDto customerAddressDao, @RequestParam long addressid){
        String userEmail = authentication.getName();
        String responseMessage = customerService.updateAddress(userEmail,customerAddressDao,addressid);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    @DeleteMapping ("/customer/delete/address")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> deleteAddress(Authentication authentication,@RequestParam long address_id){
        String userEmail = authentication.getName();
        String responseMessage = customerService.deleteAddress(userEmail,address_id);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @PostMapping("customer/add/address")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> addAddress(Authentication authentication,@Valid @RequestBody AddressDto customerAddressDto){
        String userEmail = authentication.getName();
        String responseMessage = customerService.addAddress(userEmail,customerAddressDto);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

}
