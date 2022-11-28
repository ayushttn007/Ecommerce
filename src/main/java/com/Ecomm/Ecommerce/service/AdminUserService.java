package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.DTO.ResponseDTO.CustomerResponseDto;
import com.Ecomm.Ecommerce.DTO.ResponseDTO.SellerResponseDto;


import java.util.List;

public interface AdminUserService {

    public List<CustomerResponseDto> getRegisterCustomers(Integer pageNo, Integer pageSize, String sortBy);
    public List<SellerResponseDto> getRegisterSellers(Integer pageNo, Integer pageSize, String sortBy);

    public String activateUser(Long userId);
    public String deActivateUser(Long userId);
}
