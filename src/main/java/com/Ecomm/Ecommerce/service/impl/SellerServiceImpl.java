package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired UserRepo userRepo;

    @Autowired
    SellerRepo sellerRepo;
    public SellerProfileDao getSellerProfile(String userEmail) {
        User user = userRepo.findByEmail(userEmail);

        if(user == null) {
            throw new UserNotFoundException("User does not exists");
        }
        Seller seller = sellerRepo.findByUser(user);
        if(seller == null){
            throw new UserNotFoundException("User does not exists");
        }
        SellerProfileDao sellerProfile  = new SellerProfileDao();

        sellerProfile.setId(user.getId());
        sellerProfile.setFirstName(user.getFirstName());
        sellerProfile.setLastName(user.getLastName());
        sellerProfile.setEmail(user.getEmail());
        sellerProfile.set_active(user.isActive());
        sellerProfile.setCompanyContact(seller.getCompanyContact());
        sellerProfile.setCompanyName(sellerProfile.getCompanyName());
        sellerProfile.setAddress(seller.getAddress());

        return sellerProfile;

    }



}
