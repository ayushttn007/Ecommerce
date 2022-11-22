package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.SellerProfileDao;

public interface SellerService {
    public SellerProfileDao getSellerProfile(String userEmail);
}
