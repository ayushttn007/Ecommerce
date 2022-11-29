package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.ResponseDto.CustomerResponseDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.SellerResponseDto;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.CustomerRepo;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.AdminUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

     @Autowired
    MessageSource messageSource;

    protected final Log logger = LogFactory.getLog(getClass());
    public List<CustomerResponseDto> getRegisterCustomers(Integer pageNo, Integer pageSize, String sortBy){
        logger.info("Get RegisterCustomer : Execution Start");
        logger.info("PageNo : " + pageNo + "pageSize : " + pageSize + "sortBy  : "+ sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Customer> pagedResultCustomer = customerRepo.findAll(paging);
        List<CustomerResponseDto> customerDtoList =  new ArrayList<>();

        pagedResultCustomer.forEach((customer) ->
                {
                    User user  = customer.getUser();
                    CustomerResponseDto customerDto = new CustomerResponseDto();
                    customerDto.setUserid(user.getId());
                    customerDto.setFullName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                    customerDto.setEmail(user.getEmail());
                    customerDto.set_active(user.isActive());
                    customerDtoList.add(customerDto);
                }
                );
        logger.info("Get RegisterCustomer : Execution End");
        return customerDtoList;
    }

    public List<SellerResponseDto> getRegisterSellers(Integer pageNo, Integer pageSize, String sortBy) {
        logger.info("Get RegisterSeller : Execution Start");
        logger.info("PageNo : " + pageNo + "pageSize : " + pageSize + "sortBy  : "+ sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Seller> pagedResultSeller = sellerRepo.findAll(paging);
        List<SellerResponseDto> sellerDtoList =  new ArrayList<>();

        pagedResultSeller.forEach((seller) ->
                {
                    User user  = seller.getUser();
                    SellerResponseDto sellerResponseDto = new SellerResponseDto();
                    sellerResponseDto.setUserid(user.getId());
                    sellerResponseDto.setFullName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                    sellerResponseDto.setEmail(user.getEmail());
                    sellerResponseDto.set_active(user.isActive());
                    sellerResponseDto.setCompanyName(seller.getCompanyName());
                    sellerResponseDto.setAddress(seller.getAddress());
                    sellerResponseDto.setCompanyContact(seller.getCompanyContact());
                    sellerDtoList.add(sellerResponseDto);
                }
        );
        logger.info("Get RegisterSeller : Execution End");
        return sellerDtoList;
    }

    public String activateUser(Long userId) {
        logger.info("activate User : Execution Start");
        User user = userRepo.findById(userId).orElseThrow(()->
                 new UserNotFoundException(
                         messageSource.getMessage("api.error.userNotFoundById",null, Locale.ENGLISH)
                 )
        );

        if(user.isActive()) return messageSource.getMessage("api.response.user.AccountVerified",null,Locale.ENGLISH);
        user.setActive(true);
        logger.info("activate User : User activated");
        userRepo.save(user);
        emailService.sendUserActiveMail(user);
        logger.info("activate User : Send Activation Mail");
        logger.info("activate User : Execution End");
        return messageSource.getMessage("api.response.accountActivate",null,Locale.ENGLISH);
    }


    public String deActivateUser(Long userId) {
        logger.info("DeActivate User : Execution Start");
        User user = userRepo.findById(userId).orElseThrow(()->
                new UsernameNotFoundException(
                        messageSource.getMessage("api.error.userNotFoundById",null, Locale.ENGLISH)
                )
        );

        if(!(user.isActive())) return messageSource.getMessage("api.response.accountAlreadyActive",null,Locale.ENGLISH);
        user.setActive(false);
        logger.info("DeActivate User : User DeActivated");
        userRepo.save(user);
        emailService.sendUserDeactivedMail(user);
        logger.info("DeActivate User : Send Activation Mail");
        logger.info("DeActivate User : Execution End");
        return messageSource.getMessage("api.response.accountDeactivated",null,Locale.ENGLISH);
    }


}
