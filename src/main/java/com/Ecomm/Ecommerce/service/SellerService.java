package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.DTO.PasswordDto;
import com.Ecomm.Ecommerce.DTO.ResponseDTO.SellerProfileDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.AddressUpdateDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.SellerUpdateDto;

public interface SellerService {
    public SellerProfileDto getSellerProfile(String userEmail);
    public String updateSellerProfile(String userEmail, SellerUpdateDto sellerUpdateDto);

    public String updateSellerPassword(String userEmail, PasswordDto sellerPasswordDto);
    public String updateSellerAddress(String userEmail, AddressUpdateDto addressDto, long addressId);
}
