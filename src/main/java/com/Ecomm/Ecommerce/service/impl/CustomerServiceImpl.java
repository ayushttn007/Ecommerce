package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.AddressDto;
import com.Ecomm.Ecommerce.Dto.PasswordDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CustomerProfileDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.AddressUpdateDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CustomerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.InvalidException;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.repository.AddressRepo;
import com.Ecomm.Ecommerce.repository.CustomerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.CustomerService;
import com.Ecomm.Ecommerce.service.EmailService;
import com.Ecomm.Ecommerce.service.ImageService;
import com.Ecomm.Ecommerce.utils.IgnoreNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepo userRepo;

    @Autowired AddressRepo addressRepo;

    @Autowired
    MessageSource messageSource;
    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ImageService imageService;

    protected final Log logger = LogFactory.getLog(CustomerServiceImpl.class);

    public CustomerProfileDto getCustomerProfile(String userEmail){
        logger.info("Get Customer Profile : Execution Start");
        User user = userRepo.findByEmail(userEmail);

        Customer customer = customerRepo.findByUser(user);
        logger.info("Get Customer Profile : Execution Start");
        CustomerProfileDto customerProfile  = new CustomerProfileDto();

        customerProfile.setUserid(user.getId());
        customerProfile.setFirstName(user.getFirstName());
        customerProfile.setLastName(user.getLastName());
        customerProfile.setEmail(user.getEmail());
        customerProfile.set_active(user.isActive());
        customerProfile.setContact(customer.getContact());
        logger.info("Get Customer Profile : Execution End");
        return customerProfile;
    }

   public  List<Address> getCustomerAddress(String userEmail){
       logger.info("Get Customer Address : Execution Start");
            User user = userRepo.findByEmail(userEmail);
       Customer customer = customerRepo.findByUser(user);

       List<Address> customerAddresses = customer.getAddresses();
       logger.info("Get Customer Address : Execution End");
       return customerAddresses;

   }

    public String updateProfile(String userEmail, CustomerUpdateDto customerUpdateDto, MultipartFile image){
        logger.info("Update Profile : Execution Start");
        User user = userRepo.findByEmail(userEmail);
        Customer customer = user.getCustomer();

        BeanUtils.copyProperties(customerUpdateDto, user, IgnoreNull.getNullPropertyNames(customerUpdateDto));
        BeanUtils.copyProperties(customerUpdateDto, customer, IgnoreNull.getNullPropertyNames(customerUpdateDto));

        if(!image.isEmpty()) {
            if ((image.getContentType().equals("image/jpg")
                    || image.getContentType().equals("image/jpeg")
                    || image.getContentType().equals("image/png"))) {
                try {
                    logger.info("Update Profile : Image Saving");
                    imageService.saveImage(userEmail, image);
                    logger.info("Update Profile : Image Saved");
                    userRepo.save(user);
                    customerRepo.save(customer);
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
        logger.info("Update Profile : User Saved");
        customerRepo.save(customer);
        logger.info("Update Profile : Customer Saved");
        logger.info("Update Profile : Execution End");
        return messageSource.getMessage("api.response.profileUpdate",null,Locale.ENGLISH);
    }


    public String updatePassword(String userEmail, PasswordDto customerPasswordDto) {
        logger.info("Update Password : Execution Start");
        User user = userRepo.findByEmail(userEmail);
        Customer customer = user.getCustomer();
        String encodePassword = passwordEncoder.encode(customerPasswordDto.getConfirmPassword());

        String userPassword = customerPasswordDto.getPassword();
        String userConfirmPassword = customerPasswordDto.getConfirmPassword();
        if(!(userPassword.equals(userConfirmPassword))) {
            throw new PasswordNotMatchedException(
                    messageSource.getMessage("api.error.passwordNotMatched",null, Locale.ENGLISH)
            );
        }
        user.setPassword(encodePassword);
        user.setPasswordUpdateDate(new Date());
        userRepo.save(user);
        logger.info("Update Password : Save Password");
        customerRepo.save(customer);
        logger.info("Update Password : Save Customer");
        // BONUS FEATURE - SEND MAIL ON PASSWORD CHANGE
        emailService.sendPasswordChangeMail(user);
        logger.info("Update Password : Send Password Changed Mail");
        logger.info("Update Password : Execution End");
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }


    public String updateAddress(String userEmail, AddressUpdateDto customerAddress, long addressId) {
        logger.info("Update Address : Execution Start");
        Address address = addressRepo.findById(addressId).orElseThrow(
                () ->  new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                )
        );

        User user = userRepo.findByEmail(userEmail);
        Customer customer = customerRepo.findByUser(user);

        long customerId;
        if(address.getCustomer()!= null){
            customerId = address.getSeller().getId();
            logger.info("Update Address : customer ID" + customerId);
            if(customerId != customer.getId()){
                logger.info("Update Address : customer ID" + customerId + " " + customer.getId());
                logger.info("Update Address : customer ID not Matched");
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                );
            }else{
                BeanUtils.copyProperties(customerAddress, address, IgnoreNull.getNullPropertyNames(customerAddress));
                addressRepo.save(address);
                logger.info("Update Address : Address Saved");
                logger.info("Update Address : Execution End");
                return messageSource.getMessage("api.response.addressChanged",null,Locale.ENGLISH);
            }
        }else{
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }

    }

    public String deleteAddress(String userEmail,long addressId){
        logger.info("Delete Address : Execution Start");
        Address address = addressRepo.findById(addressId).orElseThrow(
                () ->  new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                )
        );

        User user = userRepo.findByEmail(userEmail);
        Customer customer = customerRepo.findByUser(user);

        long customerId;
        if(address.getCustomer()!= null){
            customerId = address.getSeller().getId();
            logger.info("Delete Address : customer ID" + customerId);
            if(customerId != customer.getId()){
                logger.info("Delete Address : customer ID" + customerId + " " + customer.getId());
                logger.info("Delete Address : customer ID not Matched");
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                );
            }else{
                 addressRepo.delete(address);
                logger.info("Delete Address : Address Deleted");
                logger.info("Delete Address : Execution End");
                return messageSource.getMessage("api.response.addressDelete", null, Locale.ENGLISH);
            }
        }else{
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }
    }

    public String addAddress(String userEmail, AddressDto customerAddressDto){
        logger.info("Add Address : Execution Start");
        User user = userRepo.findByEmail(userEmail);
        Customer customer = customerRepo.findByUser(user);

        Address address = new Address();

        address.setCountry(customerAddressDto.getCountry());
        address.setCity(customerAddressDto.getCity());
        address.setState(customerAddressDto.getState());
        address.setAddressLine(customerAddressDto.getAddressLine());
        address.setLabel(customerAddressDto.getLabel());
        address.setZipCode(customerAddressDto.getZipCode());
        address.setCustomer(customer);

        addressRepo.save(address);
        logger.info("Add Address : Address Saved");
        logger.info("Add Address : Execution End");
        return messageSource.getMessage("api.response.addressAdded",null,Locale.ENGLISH);
    }
}
