package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.entities.*;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.UserAlreadyExistsException;
import com.Ecomm.Ecommerce.repository.*;
import com.Ecomm.Ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    SellerRepo sellerRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    EmailServiceImpl emailService;



@Autowired
    BCryptPasswordEncoder passwordEncoder;
    public void registerCustomer(CustomerDto customerDto, String role,String siteUrl) throws MessagingException, UnsupportedEncodingException {

        System.out.println(customerDto);
        User user = new User();
        Role newRole =new Role();
        newRole = roleRepo.findByAuthority(role);

        // Set User details
        user.setFirstName(customerDto.getFirstName());
        user.setMiddleName(customerDto.getMiddleName());
        user.setLastName(customerDto.getLastName());
        String userEmail = customerDto.getEmail();
        User userExists =   userRepo.findByEmail(userEmail);
        if(userExists!= null){
            throw new UserAlreadyExistsException("An account is already registered with given email");
        }
        user.setEmail(userEmail);
        if(!(customerDto.getPassword().equals(customerDto.getConfirmPassword()))){
            throw new PasswordNotMatchedException("Passwords do not Match");
        }
        user.setPassword(passwordEncoder.encode(customerDto.getConfirmPassword()));
        user.setRole(newRole);
        // Set customer contact
        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());

//        customerDto.getAddress().forEach(AddressDto -> {
//            Address address= new Address();
//            address.setCity(AddressDto.getCity());
//            address.setState(AddressDto.getState());
//            address.setAddressLine(AddressDto.getAddressLine());
//            address.setZipCode(AddressDto.getZipCode());
//            address.setCountry(AddressDto.getCountry());
//            address.setLabel(AddressDto.getLabel());
//            address.setCustomer(customer);
//
//            addressRepo.save(address);
//
//        });

        customer.setUser(user);
//        customerAddress.setCustomer(customer);
//       customer.setAddresses(customerAddresses);



        customerRepo.save(customer);
        userRepo.save(user);
        emailService.sendEmail(user, siteUrl);



    }

    public void registerSeller(SellerDto sellerDto, String role, String siteURL) throws MessagingException, UnsupportedEncodingException {

        // Create new user
        User user = new User();
        Role newRole = new Role();
        newRole = roleRepo.findByAuthority(role);

        // Set User details
        user.setEmail(sellerDto.getEmail());
        user.setFirstName(sellerDto.getFirstName());
        user.setMiddleName(sellerDto.getMiddleName());
        user.setLastName(sellerDto.getLastName());
        user.setPassword(sellerDto.getPassword());
        user.setRole(newRole);

        // create new seller
        Seller newSeller = new Seller();

        // Set Seller Details
        newSeller.setGst(sellerDto.getGst());
        newSeller.setCompanyContact(sellerDto.getCompanyContact());
        newSeller.setCompanyName(sellerDto.getCompanyName());

        newSeller.setUser(user);

        // Seller Address
        Address sellerAddress = new Address();

        //Set Seller Address
        sellerAddress.setCountry(sellerDto.getAddress().getCountry());
        sellerAddress.setState(sellerDto.getAddress().getState());
        sellerAddress.setCity(sellerDto.getAddress().getCity());
        sellerAddress.setAddressLine(sellerDto.getAddress().getAddressLine());
        sellerAddress.setZipCode(sellerDto.getAddress().getZipCode());
        sellerAddress.setLabel(sellerDto.getAddress().getLabel());

         sellerAddress.setSeller(newSeller);
         newSeller.setAddress(sellerAddress);


        userRepo.save(user);
        sellerRepo.save(newSeller);
        emailService.sendEmail(user,siteURL);
        addressRepo.save(sellerAddress);


    }




}
