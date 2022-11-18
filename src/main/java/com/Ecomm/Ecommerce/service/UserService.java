package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.dto.AddressDto;
import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.entities.*;
import com.Ecomm.Ecommerce.repos.*;
import com.Ecomm.Ecommerce.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    private TokenStore tokenStore;

@Autowired
    BCryptPasswordEncoder passwordEncoder;
    public void registerCustomer(CustomerDto customerDto, String role,String siteURL) throws MessagingException, UnsupportedEncodingException {

        System.out.println(customerDto);
        User user = new User();
        Role newRole =new Role();
        newRole = roleRepo.findByAuthority(role);

        // Set User details
        user.setEmail(customerDto.getEmail());
        user.setFirstName(customerDto.getFirstName());
        user.setMiddleName(customerDto.getMiddleName());
        user.setLastName(customerDto.getLastName());
        user.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        user.setRole(newRole);
        // Set customer contact
        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        // Set Customer Addresses
//        Address customerAddress = new Address();
//        // List to save customer addresses
//        List<Address> customerAddresses = new ArrayList<>();

//        // Set Customer Address
//        customerAddress.setCountry(customerDto.getAddress().getCountry());
//        customerAddress.setState(customerDto.getAddress().getState());
//        customerAddress.setCity(customerDto.getAddress().getCity());
//        customerAddress.setAddressLine(customerDto.getAddress().getAddressLine());
//        customerAddress.setLabel(customerDto.getAddress().getLabel());
//        customerAddress.setZipCode(customerDto.getAddress().getZipCode());
//        customerAddresses.add(customerAddress);

        customer.setUser(user);
//        customerAddress.setCustomer(customer);
//       customer.setAddresses(customerAddresses);



        emailService.register(user,siteURL);
//        emailService.sendEmailVerification(user,siteURL);
        userRepo.save(user);
        customerRepo.save(customer);
//        addressRepo.save(customerAddress);


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

        emailService.register(user,siteURL);
        userRepo.save(user);
        sellerRepo.save(newSeller);
        addressRepo.save(sellerAddress);


    }

public ResponseEntity<String> logOut(HttpServletRequest request){
    try {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenValue = authorization.replace("Bearer", "").trim();

            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);

            //OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(tokenValue);
            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            tokenStore.removeRefreshToken(refreshToken);
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Invalid access token");
    }


    return ResponseEntity.ok().body("Logout successfully");
}


}
