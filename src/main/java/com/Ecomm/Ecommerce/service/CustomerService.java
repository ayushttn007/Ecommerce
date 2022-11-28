package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.DTO.AddressDto;
import com.Ecomm.Ecommerce.DTO.PasswordDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.AddressUpdateDto;
import com.Ecomm.Ecommerce.DTO.ResponseDTO.CustomerProfileDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.CustomerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;

import java.util.List;

public interface CustomerService {
  public CustomerProfileDto getCustomerProfile(String userEmail);

   public List<Address> getCustomerAddress(String userEmail);

    public String updateProfile(String userEmail, CustomerUpdateDto customerProfileDto);

    public String updatePassword(String userEmail, PasswordDto customerPasswordDto);

    public String deleteAddress(String userEmail,long address_id);

   public String updateAddress(String userEmail, AddressUpdateDto customerAddress, long addressid);

   public String addAddress(String userEmail, AddressDto customerAddressDto);
}
