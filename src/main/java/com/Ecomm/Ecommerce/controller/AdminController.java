package com.Ecomm.Ecommerce.controller;

import com.Ecomm.Ecommerce.Dao.CustomerDao;
import com.Ecomm.Ecommerce.Dao.SellerDao;
import com.Ecomm.Ecommerce.service.AdminUserService;
import com.Ecomm.Ecommerce.service.impl.AdminUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.SecondaryTable;

import java.util.List;

@RestController
public class AdminController {


    // using hateOs entityLinks
    @Autowired private EntityLinks links;

    @Autowired
    AdminUserServiceImpl adminUserService;

    // Api to list all the register customers
    @GetMapping(path = "/admin/customers")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <List<CustomerDao>> getAllCustomers() {
        List< CustomerDao > customers = adminUserService.getRegisterCustomers();
        return new ResponseEntity<>(customers,HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/admin/sellers")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <List<SellerDao>> getAllSellers() {
        List< SellerDao > sellers = adminUserService.getRegisterSellers();
        return new ResponseEntity<>(sellers,HttpStatus.ACCEPTED);
    }


}
