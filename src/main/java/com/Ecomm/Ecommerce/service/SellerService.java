package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.PasswordDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.SellerProfileDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.AddressUpdateDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.SellerUpdateDto;

public interface SellerService {
    public SellerProfileDto getSellerProfile(String userEmail);
    public String updateSellerProfile(String userEmail, SellerUpdateDto sellerUpdateDto);

    public String updateSellerPassword(String userEmail, PasswordDto sellerPasswordDto);
    public String updateSellerAddress(String userEmail, AddressUpdateDto addressDto, long addressId);
}
