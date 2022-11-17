package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.dto.AddressDto;
import com.Ecomm.Ecommerce.dto.CustomerDto;
import com.Ecomm.Ecommerce.dto.SellerDto;
import com.Ecomm.Ecommerce.entities.*;
import com.Ecomm.Ecommerce.repos.*;
import com.Ecomm.Ecommerce.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserService {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    SellerRepo sellerRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    EmailService emailService;


    public void registerCustomer(CustomerDto customerDto, String role,String siteURL) throws MessagingException, UnsupportedEncodingException {

        System.out.println(customerDto);
        User user = new User();
        Role newRole =new Role();
        newRole = roleRepo.findByAuthority(role);

        // Set User details
        user.setEmail(customerDto.getEmail());
        user.setFirstName(customerDto.getFirstName());
        user.setMiddleName(customerDto.getMiddleName());
        user.setLastName(customerDto.getLastName());
        user.setPassword(customerDto.getPassword());
        user.setRole(newRole);
        // Set customer contact
        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        // Set Customer Addresses
//        Address customerAddress = new Address();
//        // List to save customer addresses
//        List<Address> customerAddresses = new ArrayList<>();

//        // Set Customer Address
//        customerAddress.setCountry(customerDto.getAddress().getCountry());
//        customerAddress.setState(customerDto.getAddress().getState());
//        customerAddress.setCity(customerDto.getAddress().getCity());
//        customerAddress.setAddressLine(customerDto.getAddress().getAddressLine());
//        customerAddress.setLabel(customerDto.getAddress().getLabel());
//        customerAddress.setZipCode(customerDto.getAddress().getZipCode());
//        customerAddresses.add(customerAddress);

        customer.setUser(user);
//        customerAddress.setCustomer(customer);
//       customer.setAddresses(customerAddresses);



        emailService.register(user,siteURL);
        emailService.sendEmailVerification(user,siteURL);
        userRepo.save(user);
        customerRepo.save(customer);
//        addressRepo.save(customerAddress);


    }

    public void registerSeller(SellerDto sellerDto, String role) {

        // Create new user
        User user = new User();
        Role newRole = new Role();
        newRole = roleRepo.findByAuthority(role);

        // Set User details
        user.setEmail(sellerDto.getEmail());
        user.setFirstName(sellerDto.getFirstName());
        user.setMiddleName(sellerDto.getMiddleName());
        user.setLastName(sellerDto.getLastName());
        user.setPassword(sellerDto.getPassword());
        user.setRole(newRole);

        // create new seller
        Seller newSeller = new Seller();

        // Set Seller Details
        newSeller.setGst(sellerDto.getGst());
        newSeller.setCompanyContact(sellerDto.getCompanyContact());
        newSeller.setCompanyName(sellerDto.getCompanyName());
        newSeller.setUser(user);

        // Seller Address
        Address sellerAddress = new Address();

        //Set Seller Address
        sellerAddress.setCountry(sellerDto.getAddress().getCountry());
        sellerAddress.setState(sellerDto.getAddress().getState());
        sellerAddress.setCity(sellerDto.getAddress().getCity());
        sellerAddress.setAddressLine(sellerDto.getAddress().getAddressLine());
        sellerAddress.setZipCode(sellerDto.getAddress().getZipCode());
        sellerAddress.setLabel(sellerDto.getAddress().getLabel());

         sellerAddress.setSeller(newSeller);
     newSeller.setAddress(sellerAddress);

        userRepo.save(user);
        sellerRepo.save(newSeller);
        addressRepo.save(sellerAddress);


    }

    public boolean verify(String verificationCode)  {
        User user = userRepo.findByVerificationCode(verificationCode);
        String verificationTokenDate = user.getCreated_at();
        String currentDate = emailService.getCurrentTimeUsingCalendar();
        boolean istokenActive = finddateAndtimeDifference(verificationTokenDate,currentDate);
       if(!istokenActive){
           user.setCreated_at(null);
           user.setVerificationCode(null);
           return false;
       }
       else if (user == null || user.isActive()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setActive(true);
            userRepo.save(user);
            return true;
        }

    }

    public boolean finddateAndtimeDifference(String verificationTokenDate,
                               String currentDate)  {
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss");

        // Try Class

            // parse method is used to parse
            // the text from a string to
            // produce the date
        try {
            Date tokenDate = sdf.parse(verificationTokenDate);
            Date currentdate = sdf.parse(currentDate);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time = currentdate.getTime() - tokenDate.getTime();

            // Calucalte time difference in seconds,
            // minutes, hours, years, and days
//            long difference_In_Seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time) % 60;

            long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;

//            long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;

            long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

            long difference_In_Years = TimeUnit.MILLISECONDS.toDays(difference_In_Time) / 365l;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
            boolean isValid;
            if (difference_In_Years > 0 || difference_In_Days > 0) {
                isValid = false;
            } else if (difference_In_Minutes > 1) {
                isValid = false;
            } else {
                isValid = true;
            }
            return isValid;
        }catch (ParseException e) {
            e.printStackTrace();
        }

            // Print result
//                  long leftminutes =  difference_In_Years  + difference_In_Days + difference_In_Hours + difference_In_Minutes + difference_In_Seconds;
//                  return leftminutes;


        return false;
    }
}
