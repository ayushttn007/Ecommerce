package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dao.CustomerDao;
import com.Ecomm.Ecommerce.Dao.SellerDao;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.repository.CustomerRepo;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.AdminUserService;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

     @Autowired
    CustomerRepo customerRepo;

     @Autowired
    SellerRepo sellerRepo;

     @Autowired
    UserRepo userRepo;

     @Autowired
     EmailServiceImpl emailService;
    public List<CustomerDao> getRegisterCustomers(){
        List<Customer> customersList = customerRepo.findAll();
        List<CustomerDao> customerDaoList =  new ArrayList<>();

        customersList.forEach((customer) ->
                {
                    User user  = customer.getUser();
                    CustomerDao customerDao = new CustomerDao();
                    customerDao.setId(user.getId());
                    customerDao.setFullName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                    customerDao.setEmail(user.getEmail());
                    customerDao.set_active(user.isActive());
                    customerDaoList.add(customerDao);
                }
                );

        return customerDaoList;
    }

    public List<SellerDao> getRegisterSellers() {
        List<Seller> sellerLists = sellerRepo.findAll();
        System.out.println(sellerLists.toString());
        List<SellerDao> sellerDaoList =  new ArrayList<>();

        sellerLists.forEach((seller) ->
                {
                    User user  = seller.getUser();
                    SellerDao sellerDao = new SellerDao();
                    sellerDao.setId(user.getId());
                    sellerDao.setFullName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                    sellerDao.setEmail(user.getEmail());
                    sellerDao.set_active(user.isActive());
                    sellerDao.setCompanyName(seller.getCompanyName());
                    sellerDao.setAddress(seller.getAddress());
                    sellerDao.setCompanyContact(seller.getCompanyContact());
                    sellerDaoList.add(sellerDao);
                }
        );

        return sellerDaoList;
    }

    public String activateUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->
                 new UsernameNotFoundException("User does not exists")
        );

        if(user.isActive()) return "user is Already Active";
        user.setActive(true);
        userRepo.save(user);
        emailService.sendUserActiveMail(user);
        return "User active successfully";
    }


    public String deActivateUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->
                new UsernameNotFoundException("User does not exists")
        );

        if(!(user.isActive())) return "user is Already inActive";
        user.setActive(false);
        userRepo.save(user);
        emailService.sendUserDeactivedMail(user);
        return "User Deactivated successfully";
    }


    
}
