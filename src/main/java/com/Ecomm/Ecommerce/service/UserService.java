package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.DTO.CustomerDto;
import com.Ecomm.Ecommerce.DTO.SellerDto;

public interface UserService {
    public void registerSeller(SellerDto sellerDto, String role);
    public void registerCustomer(CustomerDto customerDto, String role, String siteURL);

}
