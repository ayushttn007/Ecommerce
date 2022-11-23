package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.CustomerProfileDao;
import com.Ecomm.Ecommerce.Dao.SellerAddressDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.entities.Address;

import java.util.List;

public interface CustomerService {
    CustomerProfileDao getCustomerProfile(String userEmail);

    List<Address> getCustomerAddress(String userEmail, SellerAddressDao customerAddressDao);
}
