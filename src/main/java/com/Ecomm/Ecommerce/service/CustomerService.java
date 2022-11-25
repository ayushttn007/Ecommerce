package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.CustomerProfileDao;
import com.Ecomm.Ecommerce.Dao.CustomerUpdateDao;
import com.Ecomm.Ecommerce.Dao.PasswordDao;
import com.Ecomm.Ecommerce.Dao.AddressDao;
import com.Ecomm.Ecommerce.entities.Address;

import java.util.List;

public interface CustomerService {
  public  CustomerProfileDao getCustomerProfile(String userEmail);

   public List<Address> getCustomerAddress(String userEmail);

    public String updateProfile(String userEmail, CustomerUpdateDao customerProfileDao);

    public String updatePassword(String userEmail, PasswordDao customerPasswordDao);

    public String deleteAddress(String userEmail,long address_id);

   public String updateAddress(String userEmail, AddressDao customerAddressDao, long addressid);

   public String addAddress(String userEmail, AddressDao customerAddressDao);
}
