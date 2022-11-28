package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.DTO.AddressDto;
import com.Ecomm.Ecommerce.DTO.PasswordDto;
import com.Ecomm.Ecommerce.DTO.ResponseDTO.CustomerProfileDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.AddressUpdateDto;
import com.Ecomm.Ecommerce.DTO.UpdateDTO.CustomerUpdateDto;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.repository.AddressRepo;
import com.Ecomm.Ecommerce.repository.CustomerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.CustomerService;
import com.Ecomm.Ecommerce.service.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.PropertyDescriptor;
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

    public CustomerProfileDto getCustomerProfile(String userEmail){
        User user = userRepo.findByEmail(userEmail);

        Customer customer = customerRepo.findByUser(user);
        CustomerProfileDto customerProfile  = new CustomerProfileDto();

        customerProfile.setUserid(user.getId());
        customerProfile.setFirstName(user.getFirstName());
        customerProfile.setLastName(user.getLastName());
        customerProfile.setEmail(user.getEmail());
        customerProfile.set_active(user.isActive());
        customerProfile.setContact(customer.getContact());
        return customerProfile;
    }

   public  List<Address> getCustomerAddress(String userEmail){
            User user = userRepo.findByEmail(userEmail);
       Customer customer = customerRepo.findByUser(user);

       List<Address> customerAddresses = customer.getAddresses();
       return customerAddresses;

   }

    public String updateProfile(String userEmail, CustomerUpdateDto customerUpdateDto){
        User user = userRepo.findByEmail(userEmail);
        Customer customer = user.getCustomer();

        BeanUtils.copyProperties(customerUpdateDto, user, getNullPropertyNames(customerUpdateDto));
        BeanUtils.copyProperties(customerUpdateDto, customer, getNullPropertyNames(customerUpdateDto));

        userRepo.save(user);
        customerRepo.save(customer);
        return messageSource.getMessage("api.response.profileUpdate",null,Locale.ENGLISH);
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

    public String updatePassword(String userEmail, PasswordDto customerPasswordDto) {
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
        customerRepo.save(customer);
        // BONUS FEATURE - SEND MAIL ON PASSWORD CHANGE
        emailService.sendPasswordChangeMail(user);
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }


    public String updateAddress(String userEmail, AddressUpdateDto customerAddress, long addressId) {
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
            if(customerId != customer.getId()){
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                );
            }else{
                BeanUtils.copyProperties(customerAddress, address, getNullPropertyNames(customerAddress));
                addressRepo.save(address);
                return messageSource.getMessage("api.response.addressChanged",null,Locale.ENGLISH);
            }
        }else{
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }

    }

    public String deleteAddress(String userEmail,long addressId){
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
            if(customerId != customer.getId()){
                throw new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                );
            }else{
                 addressRepo.delete(address);
                return messageSource.getMessage("api.response.addressDelete", null, Locale.ENGLISH);
            }
        }else{
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }
    }

    public String addAddress(String userEmail, AddressDto customerAddressDto){
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
        return messageSource.getMessage("api.response.addressAdded",null,Locale.ENGLISH);
    }
}
