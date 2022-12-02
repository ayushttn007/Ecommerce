package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.PasswordDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.SellerProfileDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.AddressUpdateDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.SellerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.InvalidException;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.AddressRepo;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.ImageService;
import com.Ecomm.Ecommerce.service.SellerService;
import com.Ecomm.Ecommerce.utils.IgnoreNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

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

    @Autowired
    ImageService imageService;

    protected final Log logger = LogFactory.getLog(getClass());

    public SellerProfileDto getSellerProfile(String userEmail) {
        logger.info("get Seller Profile : Execution Start");
        User user = userRepo.findByEmail(userEmail);

        if(user == null) {
            logger.info("get Seller Profile : User is null");
            throw new UserNotFoundException(
                    messageSource.getMessage("api.error.userNotFound",null,Locale.ENGLISH)
            );
        }
        Seller seller = sellerRepo.findByUser(user);
        if(seller == null){
            logger.info("get Seller Profile : Seller is null");
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

        byte[] userImage = imageService.getImage(userEmail);
        if(userImage.length !=0){
            sellerProfile.setImage(imageService.getImage(userEmail));
        }

        logger.info("get Seller Profile : Execution End");
        return sellerProfile;

    }


    public String updateSellerProfile(String userEmail, SellerUpdateDto sellerUpdateDto,  MultipartFile image) {
        logger.info("Update Seller Profile : Execution Start");
        User user = userRepo.findByEmail(userEmail);
        Seller seller = user.getSeller();

        BeanUtils.copyProperties(sellerUpdateDto, user, IgnoreNull.getNullPropertyNames(sellerUpdateDto));
        BeanUtils.copyProperties(sellerUpdateDto, seller, IgnoreNull.getNullPropertyNames(sellerUpdateDto));

        if(!image.isEmpty()) {
            if ((image.getContentType().equals("image/jpg")
                    || image.getContentType().equals("image/jpeg")
                    || image.getContentType().equals("image/png"))) {
                try {
                    logger.info("Update Profile : Image Saving");
                    imageService.saveImage(userEmail, image);
                    logger.info("Update Profile : Image Saved");
                    userRepo.save(user);
                    sellerRepo.save(seller);
                    logger.info("Update Profile : customer & User Saved");
                    logger.info("Update Profile : Execution End");
                    return messageSource.getMessage("api.response.profileUpdate",null, Locale.ENGLISH);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new InvalidException(messageSource.getMessage("api.error.InvalidFile",null, Locale.ENGLISH));
            }
        }

        userRepo.save(user);
        logger.info("Update Seller Profile : User Save");
        sellerRepo.save(seller);
        logger.info("Update Seller Profile : Seller Save");
        logger.info("Update Seller Profile : Execution End");
        return messageSource.getMessage("api.response.profileUpdate",null,Locale.ENGLISH);
    }


    public String updateSellerPassword(String userEmail, PasswordDto sellerPasswordDto) {
        logger.info("Update Seller Password : Execution Start");
        User user = userRepo.findByEmail(userEmail);
        Seller seller = user.getSeller();
        String encodePassword = passwordEncoder.encode(sellerPasswordDto.getConfirmPassword());

        String userPassword = sellerPasswordDto.getPassword();
        String userConfirmPassword = sellerPasswordDto.getConfirmPassword();
        if(!(userPassword.equals(userConfirmPassword))) {
            logger.info("Update Seller Password : Exception password does not matched");
            throw new PasswordNotMatchedException(
                    messageSource.getMessage("api.error.passwordNotMatched",null, Locale.ENGLISH)
            );
        }

        user.setPassword(encodePassword);
        user.setPasswordUpdateDate(new Date());
        userRepo.save(user);
        sellerRepo.save(seller);
        // BONUS FEATURE - SEND MAIL ON PASSWORD CHANGE
        logger.info("Update Seller Password : Sending password change mail");
        emailService.sendPasswordChangeMail(user);
        logger.info("Update Seller Password : Execution End");
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }

    public String updateSellerAddress(String userEmail, AddressUpdateDto addressDto, long addressId) {
        logger.info("Update Seller Address : Execution Start");
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
                logger.info("Update Seller Address : throw Exception,sellerId != seller.getId()");
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                );
            }else{
                BeanUtils.copyProperties(addressDto, address, IgnoreNull.getNullPropertyNames(addressDto));
                addressRepo.save(address);
                logger.info("Update Seller Address : address save");
                return messageSource.getMessage("api.response.addressChanged",null,Locale.ENGLISH);
            }
        }else{
            logger.info("Update Seller Address : throw Exception,address.getSeller == null");
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }

    }
}
