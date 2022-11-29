package com.Ecomm.Ecommerce.controller;

import com.Ecomm.Ecommerce.Dto.ResponseDto.CustomerResponseDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.SellerResponseDto;
import com.Ecomm.Ecommerce.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {


    // using hateOs entityLinks
    @Autowired private EntityLinks links;

    @Autowired
    AdminUserService adminUserService;

    // Api to list all the register customers
    @GetMapping(path = "/admin/customers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <List<CustomerResponseDto>> getAllCustomers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(defaultValue = "id") String sortBy) {
        List<CustomerResponseDto> customers = adminUserService.getRegisterCustomers(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(customers,HttpStatus.OK);
    }

    @GetMapping(path = "/admin/sellers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <List<SellerResponseDto>> getAllSellers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortBy) {
        List< SellerResponseDto > sellers = adminUserService.getRegisterSellers(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(sellers,HttpStatus.OK);
    }

    @PatchMapping (path = "/admin/activate/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> activateUser(@RequestParam("user_id") Long userId){
        String responseMessage = adminUserService.activateUser(userId);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }


    @PatchMapping(path = "/admin/de-activate/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deactivateUser(@RequestParam("user_id") Long userId){
        String responseMessage = adminUserService.deActivateUser(userId);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }


}
