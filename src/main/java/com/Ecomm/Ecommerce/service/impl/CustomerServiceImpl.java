package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dao.CustomerDao;
import com.Ecomm.Ecommerce.Dao.CustomerProfileDao;
import com.Ecomm.Ecommerce.Dao.SellerAddressDao;
import com.Ecomm.Ecommerce.Dao.SellerProfileDao;
import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.AddressRepo;
import com.Ecomm.Ecommerce.repository.CustomerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.CustomerService;
import com.Ecomm.Ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

   public  List<Address> getCustomerAddress(String userEmail, SellerAddressDao customerAddressDao){
            User user = userRepo.findByEmail(userEmail);
       Customer customer = customerRepo.findByUser(user);

       List<Address> customerAddresses = customer.getAddresses();
       List<Address> responseAddress = new ArrayList<>();
       customerAddresses.forEach((customerAddress) ->
                {
                   Address address = new Address();
                   address.setCountry(customerAddress.getCountry());
                   address.setCity(customerAddress.getCity());
                   address.setAddressLine(customerAddress.getAddressLine());
                   address.setZipCode(customerAddress.getZipCode());
                   address.setLabel(customerAddress.getLabel());
                   address.setState(customerAddress.getState());

                   responseAddress.add(address);
                }
        );


       return responseAddress;



   }

}
