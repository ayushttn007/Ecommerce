package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.PasswordDao;
import com.Ecomm.Ecommerce.Dao.SellerAddressDao;
import com.Ecomm.Ecommerce.Dao.SellerDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.dto.UserDto;

public interface SellerService {
    public SellerProfileDao getSellerProfile(String userEmail);
    public String updateSellerProfile(String userEmail, SellerProfileDao sellerProfileDao);

    public String updateSellerPassword(String userEmail, PasswordDao sellerPasswordDao);
    public String updateSellerAddress(String userEmail, SellerAddressDao sellerAddressDao, long addressId);
}
