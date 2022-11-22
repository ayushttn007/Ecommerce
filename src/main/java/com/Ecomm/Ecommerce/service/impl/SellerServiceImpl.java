package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dao.PasswordDao;
import com.Ecomm.Ecommerce.Dao.SellerDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.dto.UserDto;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.SellerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    MessageSource messageSource;


    @Autowired EmailServiceImpl emailService;
    public SellerProfileDao getSellerProfile(String userEmail) {
        User user = userRepo.findByEmail(userEmail);

        if(user == null) {
            throw new UserNotFoundException(
                    messageSource.getMessage("api.error.userNotFound",null,Locale.ENGLISH)
            );
        }
        Seller seller = sellerRepo.findByUser(user);
        if(seller == null){
            throw new UserNotFoundException(
                    messageSource.getMessage("api.error.userNotFound",null,Locale.ENGLISH)
            );
        }
        SellerProfileDao sellerProfile  = new SellerProfileDao();

        sellerProfile.setFirstName(user.getFirstName());
        sellerProfile.setLastName(user.getLastName());
        sellerProfile.setEmail(user.getEmail());
        sellerProfile.set_active(user.isActive());
        sellerProfile.setCompanyContact(seller.getCompanyContact());
        sellerProfile.setCompanyName(sellerProfile.getCompanyName());
        sellerProfile.setAddress(seller.getAddress());

        return sellerProfile;

    }




    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    public String updateSellerProfile(String userEmail, SellerProfileDao sellerProfileDao) {
        User user = userRepo.findByEmail(userEmail);
        Seller seller = user.getSeller();


        BeanUtils.copyProperties(sellerProfileDao, user, getNullPropertyNames(sellerProfileDao));
        BeanUtils.copyProperties(sellerProfileDao, seller, getNullPropertyNames(sellerProfileDao));

        userRepo.save(user);
        sellerRepo.save(seller);
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }


    public String updateSellerPassword(String userEmail, PasswordDao sellerPasswordDao) {
        User user = userRepo.findByEmail(userEmail);
        Seller seller = user.getSeller();
        //check old password with new one.
        //BONUS FEATURE
         if(!(user.getPassword().equals(sellerPasswordDao.getOldPassword()))){
             throw new PasswordNotMatchedException(
                     messageSource.getMessage("api.error.passwordNotMatched",null, Locale.ENGLISH)
             );
         }

        BeanUtils.copyProperties(sellerPasswordDao, user, getNullPropertyNames(sellerPasswordDao));
        BeanUtils.copyProperties(sellerPasswordDao, seller, getNullPropertyNames(sellerPasswordDao));

        user.setPasswordUpdateDate(new Date());
        userRepo.save(user);
        sellerRepo.save(seller);
        emailService.sendPasswordChangeMail(user);
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }
}
