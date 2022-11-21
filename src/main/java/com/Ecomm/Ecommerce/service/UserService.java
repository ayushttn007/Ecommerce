package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface UserService {
    public void registerSeller(SellerDto sellerDto, String role);
    public void registerCustomer(CustomerDto customerDto, String role, String siteURL);

}
