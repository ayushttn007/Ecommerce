package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.DTO.PasswordDto;
import com.Ecomm.Ecommerce.DTO.ResponseDTO.SellerProfileDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.AddressUpdateDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.SellerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.AddressRepo;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.SellerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired EmailServiceImpl emailService;
    public SellerProfileDto getSellerProfile(String userEmail) {
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
        SellerProfileDto sellerProfile  = new SellerProfileDto();

        sellerProfile.setFirstName(user.getFirstName());
        sellerProfile.setLastName(user.getLastName());
        sellerProfile.set_active(user.isActive());
        sellerProfile.setGst(seller.getGst());
        sellerProfile.setUserid(user.getId());
        sellerProfile.setCompanyContact(seller.getCompanyContact());
        sellerProfile.setCompanyName(seller.getCompanyName());
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

    public String updateSellerProfile(String userEmail, SellerUpdateDto sellerUpdateDto) {
        User user = userRepo.findByEmail(userEmail);
        Seller seller = user.getSeller();


        BeanUtils.copyProperties(sellerUpdateDto, user, getNullPropertyNames(sellerUpdateDto));
        BeanUtils.copyProperties(sellerUpdateDto, seller, getNullPropertyNames(sellerUpdateDto));

        userRepo.save(user);
        sellerRepo.save(seller);
        return messageSource.getMessage("api.response.profileUpdate",null,Locale.ENGLISH);
    }


    public String updateSellerPassword(String userEmail, PasswordDto sellerPasswordDto) {
        User user = userRepo.findByEmail(userEmail);
        Seller seller = user.getSeller();
        String encodePassword = passwordEncoder.encode(sellerPasswordDto.getConfirmPassword());

        String userPassword = sellerPasswordDto.getPassword();
        String userConfirmPassword = sellerPasswordDto.getConfirmPassword();
        if(!(userPassword.equals(userConfirmPassword))) {
            throw new PasswordNotMatchedException(
                    messageSource.getMessage("api.error.passwordNotMatched",null, Locale.ENGLISH)
            );
        }

//        BeanUtils.copyProperties(sellerPasswordDao, user, getNullPropertyNames(sellerPasswordDao));
//        BeanUtils.copyProperties(sellerPasswordDao, seller, getNullPropertyNames(sellerPasswordDao));
        user.setPassword(encodePassword);
        user.setPasswordUpdateDate(new Date());
        userRepo.save(user);
        sellerRepo.save(seller);
        // BONUS FEATURE - SEND MAIL ON PASSWORD CHANGE
        emailService.sendPasswordChangeMail(user);
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }

    public String updateSellerAddress(String userEmail, AddressUpdateDto addressDto, long addressId) {
        Address address = addressRepo.findById(addressId).orElseThrow(
                () ->  new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                )
        );


        User user = userRepo.findByEmail(userEmail);
        Seller seller = sellerRepo.findByUser(user);


        long sellerId;
        if(address.getSeller()!= null){
            sellerId = address.getSeller().getId();
            if(sellerId != seller.getId()){
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                );
            }else{
                BeanUtils.copyProperties(addressDto, address, getNullPropertyNames(addressDto));
                addressRepo.save(address);
                return messageSource.getMessage("api.response.addressChanged",null,Locale.ENGLISH);
            }
        }else{
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }

    }
}
