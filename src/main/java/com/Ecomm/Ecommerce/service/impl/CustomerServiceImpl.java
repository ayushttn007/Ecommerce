package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dao.*;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
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
import org.springframework.security.core.Authentication;
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

    public CustomerProfileDao getCustomerProfile(String userEmail){
        User user = userRepo.findByEmail(userEmail);

//        if(user == null) {
//            throw new UserNotFoundException(
//                    messageSource.getMessage("api.error.userNotFound",null, Locale.ENGLISH)
//            );
//        }
        Customer customer = customerRepo.findByUser(user);
//        List<Address> customerAddresses = customer.getAddresses();
//        if(customer == null){
//            throw new UserNotFoundException(
//                    messageSource.getMessage("api.error.userNotFound",null,Locale.ENGLISH)
//            );
//        }
        CustomerProfileDao customerProfile  = new CustomerProfileDao();

        customerProfile.setUserid(user.getId());
        customerProfile.setFirstName(user.getFirstName());
        customerProfile.setLastName(user.getLastName());
        customerProfile.setEmail(user.getEmail());
        customerProfile.set_active(user.isActive());
        customerProfile.setContact(customer.getContact());
//        List<Address> customerResponseList = new ArrayList<>();
//        customerAddresses.forEach((customerAddress) ->
//                {
//                   Address address = new Address();
//                   address.setCountry(customerAddress.getCountry());
//                   address.setCity(customerAddress.getCity());
//                   address.setAddressLine(customerAddress.getAddressLine());
//                   address.setZipCode(customerAddress.getZipCode());
//                   address.setLabel(customerAddress.getLabel());
//                   address.setState(customerAddress.getState());
//
//                   customerResponseList.add(address);
//                }
//        );
//        customerProfile.setAddresses(customerResponseList);
        return customerProfile;
    }

   public  List<Address> getCustomerAddress(String userEmail){
            User user = userRepo.findByEmail(userEmail);
       Customer customer = customerRepo.findByUser(user);

       List<Address> customerAddresses = customer.getAddresses();
//       List<Address> responseAddress = new ArrayList<>();
//       customerAddresses.forEach((customerAddress) ->
//                {
//                   Address address = new Address();
//                   address.setCountry(customerAddress.getCountry());
//                   address.setCity(customerAddress.getCity());
//                   address.setAddressLine(customerAddress.getAddressLine());
//                   address.setZipCode(customerAddress.getZipCode());
//                   address.setLabel(customerAddress.getLabel());
//                   address.setState(customerAddress.getState());
//
//                   responseAddress.add(address);
//                }
//        );
       return customerAddresses;


   }

    public String updateProfile(String userEmail, CustomerProfileDao customerProfileDao){
        User user = userRepo.findByEmail(userEmail);
        Customer customer = user.getCustomer();

        BeanUtils.copyProperties(customerProfileDao, user, getNullPropertyNames(customerProfileDao));
        BeanUtils.copyProperties(customerProfileDao, customer, getNullPropertyNames(customerProfileDao));

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

    public String updatePassword(String userEmail, PasswordDao sellerPasswordDao) {
        User user = userRepo.findByEmail(userEmail);
        Customer customer = user.getCustomer();
        String encodePassword = passwordEncoder.encode(sellerPasswordDao.getConfirmPassword());

        String userPassword = sellerPasswordDao.getPassword();
        String userConfirmPassword = sellerPasswordDao.getConfirmPassword();
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
        customerRepo.save(customer);
        // BONUS FEATURE - SEND MAIL ON PASSWORD CHANGE
        emailService.sendPasswordChangeMail(user);
        return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
    }


    public String updateAddress(String userEmail, SellerAddressDao customerAddressDao,long addressId) {
        Address address = addressRepo.findById(addressId).orElseThrow(
                () ->  new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                )
        );


        BeanUtils.copyProperties(customerAddressDao, address, getNullPropertyNames(customerAddressDao));
        addressRepo.save(address);
        return messageSource.getMessage("api.response.addressChanged",null,Locale.ENGLISH);

    }

    public String deleteAddress(String userEmail,long addressId){
        Address address = addressRepo.findById(addressId).orElseThrow(
                () ->  new ResourceNotFoundException(
                        messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
                )
        );

        User user = userRepo.findByEmail(userEmail);
        Customer customer = customerRepo.findByUser(user);
        // throw exception
        long customerid;
        if(address.getCustomer()!= null){
            customerid = address.getCustomer().getId();
        }else{
            throw  new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }



        if(customerid != customer.getId()){
            throw new ResourceNotFoundException(
                    messageSource.getMessage("api.error.addressNotFound",null,Locale.ENGLISH)
            );
        }
          else {
            addressRepo.delete(address);
            return messageSource.getMessage("api.response.addressDelete", null, Locale.ENGLISH);
        }
    }

    public String addAddress(String userEmail,SellerAddressDao customerAddressDao){
        User user = userRepo.findByEmail(userEmail);
        Customer customer = customerRepo.findByUser(user);

      //  List<Address> customerAddress  = customer.getAddresses();

        Address address = new Address();

        address.setCountry(customerAddressDao.getCountry());
        address.setCity(customerAddressDao.getCity());
        address.setState(customerAddressDao.getState());
        address.setAddressLine(customerAddressDao.getAddressLine());
        address.setLabel(customerAddressDao.getLabel());
        address.setZipCode(customerAddressDao.getZipCode());
        address.setCustomer(customer);

        addressRepo.save(address);
        return messageSource.getMessage("api.response.addressAdded",null,Locale.ENGLISH);
    }
}
