package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dao.CustomerDao;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminUserService {

    public List<CustomerDao> getRegisterCustomers();
}
