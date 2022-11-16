package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Role;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.repos.CustomerRepo;
import com.Ecomm.Ecommerce.repos.RoleRepo;
import com.Ecomm.Ecommerce.repos.SellerRepo;
import com.Ecomm.Ecommerce.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    SellerRepo sellerRepo;

    @Autowired
    UserRepo userRepo;

    public void registerCustomer(CustomerDto customerDto, String role) {

        System.out.println(customerDto);
        User user = new User();
        Role newRole =new Role();
        newRole = roleRepo.findByAuthority(role);

        user.setEmail(customerDto.getEmail());
        user.setFirstName(customerDto.getFirstName());
        user.setMiddleName(customerDto.getMiddleName());
        user.setLastName(customerDto.getLastName());
        user.setPassword(customerDto.getPassword());
        user.setRole(newRole);

        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        customer.setUser(user);

        customerRepo.save(customer);
        userRepo.save(user);

    }

    public void registerSeller(SellerDto sellerDto, String role) {

        User user = new User();
        Role newRole = new Role();
        newRole = roleRepo.findByAuthority(role);

        user.setEmail(sellerDto.getEmail());
        user.setFirstName(sellerDto.getFirstName());
        user.setMiddleName(sellerDto.getMiddleName());
        user.setLastName(sellerDto.getLastName());
        user.setPassword(sellerDto.getPassword());
        user.setRole(newRole);

        Seller newSeller = new Seller();

        newSeller.setGst(sellerDto.getGst());
        newSeller.setCompanyContact(sellerDto.getCompanyContact());
        newSeller.setCompanyName(sellerDto.getCompanyName());
        newSeller.setUser(user);

        sellerRepo.save(newSeller);
        userRepo.save(user);


    }
}
