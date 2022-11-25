package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.*;

public interface SellerService {
    public SellerProfileDao getSellerProfile(String userEmail);
    public String updateSellerProfile(String userEmail, SellerUpdateDao sellerUpdateeDao);

    public String updateSellerPassword(String userEmail, PasswordDao sellerPasswordDao);
    public String updateSellerAddress(String userEmail, AddressDao addressDao, long addressId);
}
