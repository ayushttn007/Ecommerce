package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.CustomerDao;
import com.Ecomm.Ecommerce.Dao.SellerDao;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminUserService {

    public List<CustomerDao> getRegisterCustomers();
    public List<SellerDao> getRegisterSellers();

    public String activateUser(Long userId);
    public String deActivateUser(Long userId);
}
