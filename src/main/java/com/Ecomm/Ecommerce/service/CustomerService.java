package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.AddressDto;
import com.Ecomm.Ecommerce.Dto.PasswordDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.AddressUpdateDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CustomerProfileDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CustomerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
  public CustomerProfileDto getCustomerProfile(String userEmail);

   public List<Address> getCustomerAddress(String userEmail);

    public String updateProfile(String userEmail, CustomerUpdateDto customerProfileDto, MultipartFile image);

    public String updatePassword(String userEmail, PasswordDto customerPasswordDto);

    public String deleteAddress(String userEmail,long address_id);

   public String updateAddress(String userEmail, AddressUpdateDto customerAddress, long addressid);

   public String addAddress(String userEmail, AddressDto customerAddressDto);
}
