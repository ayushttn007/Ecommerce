package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.CustomerProfileDao;
import com.Ecomm.Ecommerce.Dao.PasswordDao;
import com.Ecomm.Ecommerce.Dao.SellerAddressDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.entities.Address;

import java.util.List;

public interface CustomerService {
  public  CustomerProfileDao getCustomerProfile(String userEmail);

   public List<Address> getCustomerAddress(String userEmail);

    public String updateProfile(String userEmail, CustomerProfileDao customerProfileDao);

    public String updatePassword(String userEmail, PasswordDao sellerPasswordDao);

    public String deleteAddress(String userEmail,long address_id);

   public String updateAddress(String userEmail, SellerAddressDao customerAddressDao, long addressid);

   public String addAddress(String userEmail, SellerAddressDao customerAddressDao);
}
