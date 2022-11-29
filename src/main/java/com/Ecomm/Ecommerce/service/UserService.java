package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.CustomerDto;
import com.Ecomm.Ecommerce.Dto.SellerDto;

public interface UserService {
    public void registerSeller(SellerDto sellerDto, String role);
    public void registerCustomer(CustomerDto customerDto, String role, String siteURL);

}
